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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

        // Auto-calculate next setIndex
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
}
