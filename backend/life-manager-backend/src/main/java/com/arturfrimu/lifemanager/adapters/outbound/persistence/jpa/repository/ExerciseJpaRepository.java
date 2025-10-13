package com.arturfrimu.lifemanager.adapters.outbound.persistence.jpa.repository;

import com.arturfrimu.lifemanager.adapters.outbound.persistence.jpa.entity.ExerciseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ExerciseJpaRepository extends JpaRepository<ExerciseEntity, UUID> {
    boolean existsByName(String name);
}

