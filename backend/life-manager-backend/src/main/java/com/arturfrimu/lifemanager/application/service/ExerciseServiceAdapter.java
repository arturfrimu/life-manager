package com.arturfrimu.lifemanager.application.service;

import com.arturfrimu.lifemanager.application.port.input.ExerciseServicePort;
import com.arturfrimu.lifemanager.application.port.output.ExerciseRepositoryPort;
import com.arturfrimu.lifemanager.application.port.output.ImageMetadataRepositoryPort;
import com.arturfrimu.lifemanager.domain.model.Exercise;
import com.arturfrimu.lifemanager.domain.model.ImageMetadata;
import com.arturfrimu.lifemanager.domain.model.Type;
import com.arturfrimu.lifemanager.domain.port.output.FileStoragePort;
import com.google.common.base.Preconditions;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ExerciseServiceAdapter implements ExerciseServicePort {

    FileStoragePort fileStoragePort;
    ExerciseRepositoryPort exerciseRepositoryPort;
    ImageMetadataRepositoryPort imageMetadataRepositoryPort;

    @Override
    @Transactional
    public Exercise createExercise(String name, Type type, String description) {
        Preconditions.checkArgument(Objects.nonNull(name), "Name cannot be null");
        Preconditions.checkArgument(Objects.nonNull(type), "Type cannot be null");

        log.info("Creating exercise with name: {}", name);

        if (exerciseRepositoryPort.existsByName(name)) {
            throw new IllegalArgumentException("Exercise with name '%s' already exists".formatted(name));
        }

        var exercise = Exercise.create(name, type, description);
        var savedExercise = exerciseRepositoryPort.save(exercise);

        log.info("Successfully created exercise with id: {}", savedExercise.id());
        return savedExercise;
    }

    @Override
    @Transactional(readOnly = true)
    public Exercise getExercise(UUID id) {
        Preconditions.checkArgument(Objects.nonNull(id), "Id cannot be null");

        log.info("Retrieving exercise with id: {}", id);
        var exercise = exerciseRepositoryPort.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Exercise with id '%s' not found".formatted(id)));

        return enrichExercise(exercise);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Exercise> findAllExercises(Pageable pageable) {
        Preconditions.checkArgument(Objects.nonNull(pageable), "Pageable cannot be null");

        log.info("Retrieving exercises with page: {}, size: {}", pageable.getPageNumber(), pageable.getPageSize());
        var exercisePage = exerciseRepositoryPort.findAll(pageable);

        var exerciseIds = exercisePage.getContent().stream()
                .map(Exercise::id)
                .toList();

        var enrichedExercises = exercisePage.getContent().stream()
                .map(this::enrichExercise)
                .toList();

        return new PageImpl<>(enrichedExercises, pageable, exercisePage.getTotalElements());
    }

    @Override
    @Transactional
    public void deleteExercise(UUID id) {
        Preconditions.checkArgument(Objects.nonNull(id), "Id cannot be null");

        log.info("Deleting exercise with id: {}", id);

        if (exerciseRepositoryPort.findById(id).isEmpty()) {
            throw new IllegalArgumentException("Exercise with id '%s' not found".formatted(id));
        }

        exerciseRepositoryPort.deleteById(id);

        log.info("Successfully deleted exercise with id: {}", id);
    }

    @Override
    @Transactional
    public Exercise updateExercise(UUID id, String name, Type type, String description) {
        Preconditions.checkArgument(Objects.nonNull(id), "Id cannot be null");
        Preconditions.checkArgument(Objects.nonNull(name), "Name cannot be null");
        Preconditions.checkArgument(Objects.nonNull(type), "Type cannot be null");

        log.info("Updating exercise with id: {}", id);

        var existingExercise = exerciseRepositoryPort.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Exercise with id '%s' not found".formatted(id)));

        if (!existingExercise.name().equals(name) && exerciseRepositoryPort.existsByName(name)) {
            throw new IllegalArgumentException("Exercise with name '%s' already exists".formatted(name));
        }

        var updatedExercise = new Exercise(
                existingExercise.id(),
                name,
                type,
                description,
                existingExercise.created(),
                Instant.now()
        );

        var savedExercise = exerciseRepositoryPort.update(updatedExercise);

        log.info("Successfully updated exercise with id: {}", savedExercise.id());
        return enrichExercise(savedExercise);
    }

    @Override
    public void attachImage(UUID exerciseId, List<MultipartFile> files) {
        for (MultipartFile file : files) {
            fileStoragePort.putObject(
                    FileStoragePort.FileMetadata.create(
                            "/sport/exercises/%s/%s".formatted(exerciseId, file.getOriginalFilename()),
                            extractStream(file),
                            file.getSize(),
                            file.getContentType()
                    )
            );
            imageMetadataRepositoryPort.save(
                    ImageMetadata.create(
                            file.getOriginalFilename(),
                            file.getContentType(),
                            file.getSize()
                    )
            );
        }
    }

    private static InputStream extractStream(MultipartFile file) {
        try {
            return file.getInputStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Exercise enrichExercise(Exercise exercise) {
        return new Exercise(
                exercise.id(),
                exercise.name(),
                exercise.type(),
                exercise.description(),
                exercise.created(),
                exercise.updated()
        );
    }
}

