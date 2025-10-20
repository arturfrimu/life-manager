package com.arturfrimu.lifemanager.controller.workout;

import com.arturfrimu.lifemanager.controller.PageResponse;
import com.arturfrimu.lifemanager.repository.WorkoutSessionRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/workouts")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WorkoutController {

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
}

