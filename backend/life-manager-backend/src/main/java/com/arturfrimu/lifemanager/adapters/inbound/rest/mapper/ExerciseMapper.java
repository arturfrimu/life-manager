package com.arturfrimu.lifemanager.adapters.inbound.rest.mapper;

import com.arturfrimu.lifemanager.adapters.inbound.rest.dto.CreateExerciseRequest;
import com.arturfrimu.lifemanager.adapters.inbound.rest.dto.ExercisePageResponse;
import com.arturfrimu.lifemanager.adapters.inbound.rest.dto.ExerciseResponse;
import com.arturfrimu.lifemanager.domain.model.Exercise;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(componentModel = "spring", uses = ImageMapper.class, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ExerciseMapper {
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "images", ignore = true)
    @Mapping(target = "created", ignore = true)
    @Mapping(target = "updated", ignore = true)
    Exercise toDomain(CreateExerciseRequest request);
    
    ExerciseResponse toResponse(Exercise exercise);
    
    List<ExerciseResponse> toResponseList(List<Exercise> exercises);
    
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

