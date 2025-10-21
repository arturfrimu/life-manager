package com.arturfrimu.lifemanager.controller.workoutexercise;

import com.arturfrimu.lifemanager.entity.WorkoutExercise;
import com.arturfrimu.lifemanager.repository.ExerciseRepository;
import com.arturfrimu.lifemanager.repository.WorkoutExerciseRepository;
import com.arturfrimu.lifemanager.repository.WorkoutSessionRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/workout-exercises")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Workout Exercises", description = "API for adding exercises to workout sessions")
public class WorkoutExerciseController {

    ExerciseRepository exerciseRepository;
    WorkoutSessionRepository workoutSessionRepository;
    WorkoutExerciseRepository workoutExerciseRepository;

    @PostMapping
    @Transactional
    @Operation(
            summary = "Add exercise to workout session",
            description = "Adds an exercise to a workout session. The orderIndex is automatically calculated based on existing exercises in the workout."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Exercise added to workout successfully",
                    content = @Content(schema = @Schema(implementation = WorkoutExerciseResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request - workout session or exercise not found",
                    content = @Content
            )
    })
    public ResponseEntity<WorkoutExerciseResponse> createWorkoutExercise(
            @Valid @RequestBody CreateWorkoutExerciseRequest request
    ) {
        log.info("Received request to create workout exercise for session: {}", request.workoutSessionId());

        var workoutSession = workoutSessionRepository.findById(request.workoutSessionId())
                .orElseThrow(() -> new IllegalArgumentException("Workout session not found: " + request.workoutSessionId()));

        var exercise = exerciseRepository.findById(request.exerciseId())
                .orElseThrow(() -> new IllegalArgumentException("Exercise not found: " + request.exerciseId()));

        var existingExercises = workoutExerciseRepository.findByWorkoutSessionOrderByOrderIndexAsc(workoutSession);
        var nextOrderIndex = existingExercises.stream()
                .mapToInt(WorkoutExercise::getOrderIndex)
                .max()
                .orElse(-1) + 1;

        var workoutExercise = WorkoutExercise.builder()
                .id(UUID.randomUUID())
                .workoutSession(workoutSession)
                .exercise(exercise)
                .orderIndex(nextOrderIndex)
                .notes(request.notes())
                .build();

        var saved = workoutExerciseRepository.save(workoutExercise);

        var response = new WorkoutExerciseResponse(
                saved.getId(),
                saved.getWorkoutSession().getId(),
                saved.getExercise().getId(),
                saved.getOrderIndex(),
                saved.getNotes(),
                saved.getCreated(),
                saved.getUpdated()
        );

        log.info("Successfully created workout exercise with id: {} and order index: {}", saved.getId(), saved.getOrderIndex());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}