package com.arturfrimu.lifemanager.controller.workout;

import com.arturfrimu.lifemanager.controller.PageResponse;
import com.arturfrimu.lifemanager.entity.WorkoutSession;
import com.arturfrimu.lifemanager.repository.UserRepository;
import com.arturfrimu.lifemanager.repository.WorkoutSessionRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@CrossOrigin("*")
@Slf4j
@RestController
@RequestMapping("/api/v1/workouts")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WorkoutController {

    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.systemDefault());
    UserRepository userRepository;
    WorkoutSessionRepository workoutSessionRepository;

    @GetMapping
    public ResponseEntity<PageResponse<WorkoutSessionResponse>> findAllWorkouts(
            @PageableDefault(sort = "name") Pageable pageable
    ) {
        log.info("Received request to get workouts with page: {}, size: {}", pageable.getPageNumber(), pageable.getPageSize());

        var workoutsPage = workoutSessionRepository.findAll(pageable);

        var content = workoutsPage.getContent().stream()
                .map(workout -> new WorkoutSessionResponse(
                        workout.getId(),
                        workout.getUser() != null ? workout.getUser().getId() : null,
                        workout.getName(),
                        workout.getNotes(),
                        workout.getStartedAt(),
                        workout.getCompletedAt(),
                        workout.getCreated(),
                        workout.getUpdated()
                ))
                .toList();

        var response = new PageResponse<>(
                content,
                workoutsPage.getNumber(),
                workoutsPage.getSize(),
                workoutsPage.getTotalElements(),
                workoutsPage.getTotalPages(),
                workoutsPage.isFirst(),
                workoutsPage.isLast()
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<WorkoutSessionDetailResponse> findWorkoutById(@PathVariable UUID id) {
        log.info("Received request to get workout details for id: {}", id);

        var workout = workoutSessionRepository.findByIdWithExercises(id)
                .orElseThrow(() -> {
                    log.error("Workout with id {} not found", id);
                    return new ResponseStatusException(NOT_FOUND, "Workout not found with id: " + id);
                });
        
        // Fetch sets separately to avoid MultipleBagFetchException
        workoutSessionRepository.findWorkoutExercisesWithSets(id);

        var workoutExercises = workout.getWorkoutExercises().stream()
                .map(we -> {
                    var sets = we.getSets().stream()
                            .map(set -> new SetResponse(
                                    set.getId(),
                                    set.getSetIndex(),
                                    set.getReps(),
                                    set.getWeight(),
                                    set.getCompleted(),
                                    set.getNotes(),
                                    set.getCreated(),
                                    set.getUpdated()
                            ))
                            .sorted((s1, s2) -> s1.setIndex().compareTo(s2.setIndex()))
                            .toList();

                    var exerciseInfo = new WorkoutExerciseDetailResponse.ExerciseInfo(
                            we.getExercise().getId(),
                            we.getExercise().getName(),
                            we.getExercise().getType(),
                            we.getExercise().getDescription(),
                            we.getExercise().getImageUrl(),
                            sets
                    );

                    return new WorkoutExerciseDetailResponse(
                            we.getId(),
                            we.getOrderIndex(),
                            we.getNotes(),
                            exerciseInfo,
                            we.getCreated(),
                            we.getUpdated()
                    );
                })
                .sorted((we1, we2) -> we1.orderIndex().compareTo(we2.orderIndex()))
                .toList();

        var response = new WorkoutSessionDetailResponse(
                workout.getId(),
                workout.getUser() != null ? workout.getUser().getId() : null,
                workout.getName(),
                workout.getNotes(),
                workout.getStartedAt(),
                workout.getCompletedAt(),
                workoutExercises,
                workout.getCreated(),
                workout.getUpdated()
        );

        log.info("Successfully retrieved workout details for id: {}", id);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Transactional
    public ResponseEntity<WorkoutSessionResponse> initializeWorkout() {
        log.info("Received request to initialize new workout");

        var now = Instant.now();
        var workoutName = FORMATTER.format(now);

        var user = userRepository.findAll().stream()
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "No user found"));

        var workout = WorkoutSession.builder()
                .id(UUID.randomUUID())
                .user(user)
                .name(workoutName)
                .startedAt(now)
                .workoutExercises(List.of())
                .build();

        var saved = workoutSessionRepository.save(workout);

        var response = new WorkoutSessionResponse(
                saved.getId(),
                saved.getUser().getId(),
                saved.getName(),
                saved.getNotes(),
                saved.getStartedAt(),
                saved.getCompletedAt(),
                saved.getCreated(),
                saved.getUpdated()
        );

        log.info("Successfully initialized workout with id: {} and name: {}", saved.getId(), saved.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> deleteWorkout(@PathVariable UUID id) {
        log.info("Received request to delete workout with id: {}", id);

        var workout = workoutSessionRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Workout with id {} not found", id);
                    return new ResponseStatusException(NOT_FOUND, "Workout not found with id: " + id);
                });

        workoutSessionRepository.delete(workout);

        log.info("Successfully deleted workout with id: {}", id);
        return ResponseEntity.noContent().build();
    }
}

