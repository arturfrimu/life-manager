package com.arturfrimu.lifemanager.sport.adapter;

import com.arturfrimu.lifemanager.sport.entity.ExerciseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ExerciseJpaRepository extends JpaRepository<ExerciseEntity, UUID> {
    boolean existsByName(String name);
}
