package com.arturfrimu.lifemanager.sport.controller;

import com.arturfrimu.lifemanager.sport.dto.CreateExerciseRequest;
import com.arturfrimu.lifemanager.sport.dto.ExerciseResponse;
import com.arturfrimu.lifemanager.sport.mapper.ExerciseMapper;
import com.arturfrimu.lifemanager.sport.port.ExerciseServicePort;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/sport/exercises")
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ExerciseController {

    ExerciseServicePort exerciseServicePort;
    ExerciseMapper exerciseMapper;

    @PostMapping
    public ResponseEntity<ExerciseResponse> createExercise(@Valid @RequestBody CreateExerciseRequest request) {
        log.info("Received request to create exercise: {}", request.name());

        var exercise = exerciseServicePort.createExercise(
                request.name(),
                request.type(),
                request.description()
        );

        ExerciseResponse response = exerciseMapper.toResponse(exercise);

        log.info("Successfully created exercise with id: {}", exercise.id());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExerciseResponse> getExercise(@PathVariable UUID id) {
        log.info("Received request to get exercise with id: {}", id);

        var exercise = exerciseServicePort.getExercise(id);
        ExerciseResponse response = exerciseMapper.toResponse(exercise);

        return ResponseEntity.ok(response);
    }
}
