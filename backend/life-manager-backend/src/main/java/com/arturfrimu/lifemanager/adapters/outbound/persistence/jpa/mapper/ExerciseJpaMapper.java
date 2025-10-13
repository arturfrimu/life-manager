package com.arturfrimu.lifemanager.adapters.outbound.persistence.jpa.mapper;

import com.arturfrimu.lifemanager.adapters.outbound.persistence.jpa.entity.ExerciseEntity;
import com.arturfrimu.lifemanager.domain.model.Exercise;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ExerciseJpaMapper {
    
    ExerciseEntity toEntity(Exercise exercise);
    
    Exercise toDomain(ExerciseEntity entity);
}

