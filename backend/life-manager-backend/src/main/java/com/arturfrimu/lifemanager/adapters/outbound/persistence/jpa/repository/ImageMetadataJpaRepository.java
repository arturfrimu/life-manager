package com.arturfrimu.lifemanager.adapters.outbound.persistence.jpa.repository;

import com.arturfrimu.lifemanager.adapters.outbound.persistence.jpa.entity.ImageMetadataEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ImageMetadataJpaRepository extends JpaRepository<ImageMetadataEntity, UUID> {}

