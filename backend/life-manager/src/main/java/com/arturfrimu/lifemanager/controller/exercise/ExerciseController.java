package com.arturfrimu.lifemanager.controller.exercise;

import com.arturfrimu.lifemanager.controller.PageResponse;
import com.arturfrimu.lifemanager.repository.ExerciseRepository;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/exercises")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Exercises", description = "API for managing exercises catalog")
public class ExerciseController {

    ExerciseRepository exerciseRepository;

    @GetMapping
    @Operation(
            summary = "Get all exercises",
            description = "Retrieves a paginated list of all available exercises from the catalog. Exercises can be filtered and sorted using pagination parameters."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved exercises",
                    content = @Content(schema = @Schema(implementation = PageResponse.class))
            )
    })
    public ResponseEntity<PageResponse<ExerciseResponse>> findAllExercises(
            @Parameter(description = "Pagination parameters (page, size, sort). Default sort by name.") 
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

