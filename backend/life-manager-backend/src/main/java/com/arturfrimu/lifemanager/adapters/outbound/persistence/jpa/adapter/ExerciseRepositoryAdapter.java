package com.arturfrimu.lifemanager.adapters.outbound.persistence.jpa.adapter;

import com.arturfrimu.lifemanager.adapters.outbound.persistence.jpa.mapper.ExerciseJpaMapper;
import com.arturfrimu.lifemanager.adapters.outbound.persistence.jpa.repository.ExerciseJpaRepository;
import com.arturfrimu.lifemanager.application.port.output.ExerciseRepositoryPort;
import com.arturfrimu.lifemanager.domain.model.Exercise;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ExerciseRepositoryAdapter implements ExerciseRepositoryPort {

    ExerciseJpaRepository jpaRepository;
    ExerciseJpaMapper exerciseJpaMapper;

    @Override
    public Exercise save(Exercise exercise) {
        var entity = exerciseJpaMapper.toEntity(exercise);
        var savedEntity = jpaRepository.save(entity);
        return exerciseJpaMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Exercise> findById(UUID id) {
        return jpaRepository.findById(id)
                .map(exerciseJpaMapper::toDomain);
    }

    @Override
    public boolean existsByName(String name) {
        return jpaRepository.existsByName(name);
    }

    @Override
    public Page<Exercise> findAll(Pageable pageable) {
        return jpaRepository.findAll(pageable)
                .map(exerciseJpaMapper::toDomain);
    }

    @Override
    public void deleteById(UUID id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public Exercise update(Exercise exercise) {
        var entity = exerciseJpaMapper.toEntity(exercise);
        var savedEntity = jpaRepository.saveAndFlush(entity);
        return exerciseJpaMapper.toDomain(savedEntity);
    }
}

