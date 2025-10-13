package com.arturfrimu.lifemanager.sport.mapper;

import com.arturfrimu.lifemanager.sport.domain.Exercise;
import com.arturfrimu.lifemanager.sport.dto.CreateExerciseRequest;
import com.arturfrimu.lifemanager.sport.dto.ExercisePageResponse;
import com.arturfrimu.lifemanager.sport.dto.ExerciseResponse;
import com.arturfrimu.lifemanager.sport.entity.ExerciseEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ExerciseMapper {
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "created", ignore = true)
    @Mapping(target = "updated", ignore = true)
    Exercise toDomain(CreateExerciseRequest request);
    
    ExerciseResponse toResponse(Exercise exercise);
    
    List<ExerciseResponse> toResponseList(List<Exercise> exercises);
    
    ExerciseEntity toEntity(Exercise exercise);
    
    Exercise toDomain(ExerciseEntity entity);
    
    default ExercisePageResponse toPageResponse(Page<Exercise> page) {
        return new ExercisePageResponse(
                toResponseList(page.getContent()),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isFirst(),
                page.isLast()
        );
    }
}
