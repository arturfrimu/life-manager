package com.arturfrimu.lifemanager.application.port.output;

import com.arturfrimu.lifemanager.domain.model.Exercise;
import com.arturfrimu.lifemanager.domain.model.ImageMetadata;

import java.util.Optional;
import java.util.UUID;

public interface ImageMetadataRepositoryPort {
    ImageMetadata save(ImageMetadata imageMetadata);

    Optional<ImageMetadata> findById(UUID id);
}

