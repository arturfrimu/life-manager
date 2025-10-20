package com.arturfrimu.lifemanager.adapters.inbound.rest.controller;

import com.arturfrimu.lifemanager.adapters.inbound.rest.dto.CreateExerciseRequest;
import com.arturfrimu.lifemanager.adapters.inbound.rest.dto.ExercisePageResponse;
import com.arturfrimu.lifemanager.adapters.inbound.rest.dto.ExerciseResponse;
import com.arturfrimu.lifemanager.adapters.inbound.rest.dto.UpdateExerciseRequest;
import com.arturfrimu.lifemanager.adapters.inbound.rest.mapper.ExerciseMapper;
import com.arturfrimu.lifemanager.application.port.input.ExerciseServicePort;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/sport/exercises")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ExerciseController {

    ExerciseMapper exerciseMapper;

    ExerciseServicePort exerciseServicePort;

    @PostMapping
    public ResponseEntity<ExerciseResponse> createExercise(@Valid @RequestBody CreateExerciseRequest request) {
        log.info("Received request to create exercise: {}", request.name());

        var exercise = exerciseServicePort.createExercise(
                request.name(),
                request.type(),
                request.description()
        );

        var response = exerciseMapper.toResponse(exercise);

        log.info("Successfully created exercise with id: {}", exercise.id());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExerciseResponse> getExercise(@PathVariable UUID id) {
        log.info("Received request to get exercise with id: {}", id);

        var exercise = exerciseServicePort.getExercise(id);
        var response = exerciseMapper.toResponse(exercise);

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<ExercisePageResponse> findExercises(
            @PageableDefault(sort = "name") Pageable pageable
    ) {
        log.info("Received request to get exercises with page: {}, size: {}", pageable.getPageNumber(), pageable.getPageSize());
        
        var exercisesPage = exerciseServicePort.findAllExercises(pageable);
        var response = exerciseMapper.toPageResponse(exercisesPage);
        
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExercise(@PathVariable UUID id) {
        log.info("Received request to delete exercise with id: {}", id);
        
        exerciseServicePort.deleteExercise(id);
        
        log.info("Successfully deleted exercise with id: {}", id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExerciseResponse> updateExercise(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateExerciseRequest request
    ) {
        log.info("Received request to update exercise with id: {}", id);
        
        var exercise = exerciseServicePort.updateExercise(
                id,
                request.name(),
                request.type(),
                request.description()
        );
        
        var response = exerciseMapper.toResponse(exercise);
        
        log.info("Successfully updated exercise with id: {}", exercise.id());
        return ResponseEntity.ok(response);
    }

    @PatchMapping(value = "/{exerciseId}/images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<List<String>> attachImage(
            @PathVariable UUID exerciseId,
            @RequestParam("images") List<MultipartFile> images
    ) {
        log.info("Received request to upload {} images for exercise with id: {}", images.size(), exerciseId);
        throw new UnsupportedOperationException("Not supported yet.");
    }
}

