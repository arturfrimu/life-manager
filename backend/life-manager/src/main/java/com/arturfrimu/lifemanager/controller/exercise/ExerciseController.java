package com.arturfrimu.lifemanager.controller.exercise;

import com.arturfrimu.lifemanager.controller.PageResponse;
import com.arturfrimu.lifemanager.repository.ExerciseRepository;
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
@RequestMapping("/api/v1/exercises")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ExerciseController {

    ExerciseRepository exerciseRepository;

    @GetMapping
    public ResponseEntity<PageResponse<ExerciseResponse>> findAllExercises(
            @PageableDefault(sort = "name") Pageable pageable
    ) {
        log.info("Received request to get exercises with page: {}, size: {}", pageable.getPageNumber(), pageable.getPageSize());

        var exercisesPage = exerciseRepository.findAll(pageable);

        var content = exercisesPage.getContent().stream()
                .map(exercise -> new ExerciseResponse(
                        exercise.getId(),
                        exercise.getName(),
                        exercise.getType(),
                        exercise.getDescription(),
                        exercise.getImageUrl(),
                        exercise.getCreatedByUser() != null ? exercise.getCreatedByUser().getId() : null,
                        exercise.getCreated(),
                        exercise.getUpdated()
                ))
                .toList();

        var response = new PageResponse<>(
                content,
                exercisesPage.getNumber(),
                exercisesPage.getSize(),
                exercisesPage.getTotalElements(),
                exercisesPage.getTotalPages(),
                exercisesPage.isFirst(),
                exercisesPage.isLast()
        );

        return ResponseEntity.ok(response);
    }
}

