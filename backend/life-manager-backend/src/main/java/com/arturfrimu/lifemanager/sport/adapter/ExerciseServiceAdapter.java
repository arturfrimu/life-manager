package com.arturfrimu.lifemanager.sport.adapter;

import com.arturfrimu.lifemanager.sport.domain.Exercise;
import com.arturfrimu.lifemanager.sport.enums.Type;
import com.arturfrimu.lifemanager.sport.port.ExerciseRepositoryPort;
import com.arturfrimu.lifemanager.sport.port.ExerciseServicePort;
import com.google.common.base.Preconditions;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ExerciseServiceAdapter implements ExerciseServicePort {

    ExerciseRepositoryPort exerciseRepositoryPort;

    @Override
    @Transactional
    public Exercise createExercise(String name, Type type, String description) {
        Preconditions.checkArgument(Objects.nonNull(name), "Name cannot be null");
        Preconditions.checkArgument(Objects.nonNull(type), "Type cannot be null");

        log.info("Creating exercise with name: {}", name);

        if (exerciseRepositoryPort.existsByName(name)) {
            throw new IllegalArgumentException("Exercise with name '" + name + "' already exists");
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
        return exerciseRepositoryPort.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Exercise with id '" + id + "' not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Exercise> findAllExercises(Pageable pageable) {
        Preconditions.checkArgument(Objects.nonNull(pageable), "Pageable cannot be null");
        
        log.info("Retrieving exercises with page: {}, size: {}", pageable.getPageNumber(), pageable.getPageSize());
        return exerciseRepositoryPort.findAll(pageable);
    }
}
