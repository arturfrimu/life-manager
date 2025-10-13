package com.arturfrimu.lifemanager.sport.adapter;

import com.arturfrimu.lifemanager.sport.domain.Exercise;
import com.arturfrimu.lifemanager.sport.entity.ExerciseEntity;
import com.arturfrimu.lifemanager.sport.mapper.ExerciseMapper;
import com.arturfrimu.lifemanager.sport.port.ExerciseRepositoryPort;
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
    ExerciseMapper exerciseMapper;

    @Override
    public Exercise save(Exercise exercise) {
        var entity = exerciseMapper.toEntity(exercise);
        var savedEntity = jpaRepository.save(entity);
        return exerciseMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Exercise> findById(UUID id) {
        return jpaRepository.findById(id)
                .map(exerciseMapper::toDomain);
    }

    @Override
    public boolean existsByName(String name) {
        return jpaRepository.existsByName(name);
    }

    @Override
    public Page<Exercise> findAll(Pageable pageable) {
        return jpaRepository.findAll(pageable)
                .map(exerciseMapper::toDomain);
    }

    @Override
    public void deleteById(UUID id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public Exercise update(Exercise exercise) {
        var entity = exerciseMapper.toEntity(exercise);
        var savedEntity = jpaRepository.saveAndFlush(entity);
        return exerciseMapper.toDomain(savedEntity);
    }
}
