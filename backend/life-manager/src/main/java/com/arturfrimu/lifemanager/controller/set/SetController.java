package com.arturfrimu.lifemanager.controller.set;

import com.arturfrimu.lifemanager.entity.Set;
import com.arturfrimu.lifemanager.repository.SetRepository;
import com.arturfrimu.lifemanager.repository.WorkoutExerciseRepository;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.NOT_FOUND;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/sets")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SetController {

    SetRepository setRepository;
    WorkoutExerciseRepository workoutExerciseRepository;

    @PostMapping
    @Transactional
    public ResponseEntity<SetResponse> createSet(
            @Valid @RequestBody CreateSetRequest request
    ) {
        log.info("Received request to create set for workout exercise: {}", request.workoutExerciseId());

        var workoutExercise = workoutExerciseRepository.findById(request.workoutExerciseId())
                .orElseThrow(() -> new IllegalArgumentException("Workout exercise not found: " + request.workoutExerciseId()));

        var maxSetIndex = setRepository.findMaxSetIndexByWorkoutExerciseId(request.workoutExerciseId())
                .orElse(-1);
        var nextSetIndex = maxSetIndex + 1;

        log.info("Calculated next set index: {} for workout exercise: {}", nextSetIndex, request.workoutExerciseId());

        var set = Set.builder()
                .id(UUID.randomUUID())
                .workoutExercise(workoutExercise)
                .setIndex(nextSetIndex)
                .reps(request.reps())
                .weight(request.weight())
                .completed(false)
                .notes(null)
                .build();

        var saved = setRepository.save(set);

        var response = new SetResponse(
                saved.getId(),
                saved.getWorkoutExercise().getId(),
                saved.getSetIndex(),
                saved.getReps(),
                saved.getWeight(),
                saved.getCompleted(),
                saved.getNotes(),
                saved.getCreated(),
                saved.getUpdated()
        );

        log.info("Successfully created set with id: {} and set index: {}", saved.getId(), saved.getSetIndex());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping("/{id}/toggle-completed")
    @Transactional
    public ResponseEntity<SetResponse> toggleCompleted(@PathVariable UUID id) {
        log.info("Received request to toggle completed status for set: {}", id);

        var set = setRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Set with id {} not found", id);
                    return new ResponseStatusException(NOT_FOUND, "Set not found with id: " + id);
                });

        var previousStatus = set.getCompleted();
        set.setCompleted(!previousStatus);
        var saved = setRepository.save(set);

        var response = new SetResponse(
                saved.getId(),
                saved.getWorkoutExercise().getId(),
                saved.getSetIndex(),
                saved.getReps(),
                saved.getWeight(),
                saved.getCompleted(),
                saved.getNotes(),
                saved.getCreated(),
                saved.getUpdated()
        );

        log.info("Successfully toggled set {} completed status from {} to {}", id, previousStatus, saved.getCompleted());
        return ResponseEntity.ok(response);
    }
}
