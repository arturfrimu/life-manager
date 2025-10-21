package com.arturfrimu.lifemanager.controller.workout;

import com.arturfrimu.lifemanager.controller.PageResponse;
import com.arturfrimu.lifemanager.repository.WorkoutSessionRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Slf4j
@RestController
@RequestMapping("/api/v1/workouts")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Workout Sessions", description = "API for managing workout sessions")
public class WorkoutController {

    WorkoutSessionRepository workoutSessionRepository;

    @GetMapping
    @Operation(
            summary = "Get all workout sessions",
            description = "Retrieves a paginated list of all workout sessions with basic information (without exercises and sets)"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved workout sessions",
                    content = @Content(schema = @Schema(implementation = PageResponse.class))
            )
    })
    public ResponseEntity<PageResponse<WorkoutSessionResponse>> findAllWorkouts(
            @Parameter(description = "Pagination parameters (page, size, sort)") 
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
    @Operation(
            summary = "Get workout session details by ID",
            description = "Retrieves complete workout session details including all exercises and their sets. Uses optimized JOIN FETCH queries (2 DB queries total) to efficiently load all related data."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved workout session details",
                    content = @Content(schema = @Schema(implementation = WorkoutSessionDetailResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Workout session not found",
                    content = @Content
            )
    })
    public ResponseEntity<WorkoutSessionDetailResponse> findWorkoutById(
            @Parameter(description = "ID of the workout session to retrieve") 
            @PathVariable UUID id
    ) {
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
}

