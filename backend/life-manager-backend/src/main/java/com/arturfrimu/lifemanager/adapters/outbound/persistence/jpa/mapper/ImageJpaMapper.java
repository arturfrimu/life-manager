package com.arturfrimu.lifemanager.adapters.outbound.persistence.jpa.mapper;

import com.arturfrimu.lifemanager.adapters.outbound.persistence.jpa.entity.ImageEntity;
import com.arturfrimu.lifemanager.domain.model.Image;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ImageJpaMapper {
    
    ImageEntity toEntity(Image image);
    
    Image toDomain(ImageEntity entity);
}