package com.arturfrimu.lifemanager.adapters.outbound.persistence.jpa.mapper;

import com.arturfrimu.lifemanager.adapters.outbound.persistence.jpa.entity.ImageMetadataEntity;
import com.arturfrimu.lifemanager.domain.model.ImageMetadata;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ImageMetadataJpaMapper {
    
    ImageMetadataEntity toEntity(ImageMetadata exercise);
    
    ImageMetadata toDomain(ImageMetadataEntity entity);
}

