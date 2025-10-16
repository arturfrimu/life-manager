package com.arturfrimu.lifemanager.adapters.outbound.persistence.jpa.repository;

import com.arturfrimu.lifemanager.adapters.outbound.persistence.jpa.entity.ExerciseImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ExerciseImageJpaRepository extends JpaRepository<ExerciseImageEntity, ExerciseImageEntity.ExerciseImagesId> {
    List<ExerciseImageEntity> findByExerciseId(UUID exerciseId);
    
    List<ExerciseImageEntity> findByExerciseIdIn(List<UUID> exerciseIds);
}