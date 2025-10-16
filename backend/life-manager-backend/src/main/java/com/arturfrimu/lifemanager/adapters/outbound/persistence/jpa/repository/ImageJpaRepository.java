package com.arturfrimu.lifemanager.adapters.outbound.persistence.jpa.repository;

import com.arturfrimu.lifemanager.adapters.outbound.persistence.jpa.entity.ImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ImageJpaRepository extends JpaRepository<ImageEntity, UUID> {
    List<ImageEntity> findByIdIn(List<UUID> imageIds);
}