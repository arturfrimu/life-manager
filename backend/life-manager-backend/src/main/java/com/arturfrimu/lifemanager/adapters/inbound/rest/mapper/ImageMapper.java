package com.arturfrimu.lifemanager.adapters.inbound.rest.mapper;

import com.arturfrimu.lifemanager.adapters.inbound.rest.dto.ImageResponse;
import com.arturfrimu.lifemanager.domain.model.Image;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ImageMapper {
    
    ImageResponse toResponse(Image image);
    
    List<ImageResponse> toResponseList(List<Image> images);
}