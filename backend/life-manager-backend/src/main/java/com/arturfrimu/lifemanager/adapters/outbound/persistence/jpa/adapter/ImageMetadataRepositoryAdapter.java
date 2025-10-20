package com.arturfrimu.lifemanager.adapters.outbound.persistence.jpa.adapter;

import com.arturfrimu.lifemanager.adapters.outbound.persistence.jpa.mapper.ImageMetadataJpaMapper;
import com.arturfrimu.lifemanager.adapters.outbound.persistence.jpa.repository.ImageMetadataJpaRepository;
import com.arturfrimu.lifemanager.application.port.output.ImageMetadataRepositoryPort;
import com.arturfrimu.lifemanager.domain.model.ImageMetadata;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ImageMetadataRepositoryAdapter implements ImageMetadataRepositoryPort {

    ImageMetadataJpaRepository jpaRepository;
    ImageMetadataJpaMapper imageMetadataJpaMapper;

    @Override
    public ImageMetadata save(ImageMetadata imageMetadata) {
        var entity = imageMetadataJpaMapper.toEntity(imageMetadata);
        var savedEntity = jpaRepository.save(entity);
        return imageMetadataJpaMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<ImageMetadata> findById(UUID id) {
        return jpaRepository.findById(id)
                .map(imageMetadataJpaMapper::toDomain);
    }
}

