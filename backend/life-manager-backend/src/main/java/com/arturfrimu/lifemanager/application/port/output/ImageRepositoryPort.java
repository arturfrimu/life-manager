package com.arturfrimu.lifemanager.application.port.output;

import com.arturfrimu.lifemanager.domain.model.Image;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface ImageRepositoryPort {

    List<Image> findImagesByExerciseId(UUID exerciseId);
    
    Map<UUID, List<Image>> findImagesByExerciseIds(List<UUID> exerciseIds);
}