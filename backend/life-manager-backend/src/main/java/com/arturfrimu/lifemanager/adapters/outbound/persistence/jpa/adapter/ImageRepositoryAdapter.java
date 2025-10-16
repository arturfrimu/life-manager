package com.arturfrimu.lifemanager.adapters.outbound.persistence.jpa.adapter;

import com.arturfrimu.lifemanager.adapters.outbound.persistence.jpa.entity.ExerciseImageEntity;
import com.arturfrimu.lifemanager.adapters.outbound.persistence.jpa.entity.ImageEntity;
import com.arturfrimu.lifemanager.adapters.outbound.persistence.jpa.mapper.ImageJpaMapper;
import com.arturfrimu.lifemanager.adapters.outbound.persistence.jpa.repository.ExerciseImageJpaRepository;
import com.arturfrimu.lifemanager.adapters.outbound.persistence.jpa.repository.ImageJpaRepository;
import com.arturfrimu.lifemanager.application.port.output.ImageRepositoryPort;
import com.arturfrimu.lifemanager.domain.model.Image;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ImageRepositoryAdapter implements ImageRepositoryPort {

    ImageJpaMapper imageJpaMapper;

    ImageJpaRepository imageJpaRepository;
    ExerciseImageJpaRepository exerciseImageJpaRepository;

    @Override
    public List<Image> findImagesByExerciseId(UUID exerciseId) {
        var exerciseImageLinks = exerciseImageJpaRepository.findByExerciseId(exerciseId);
        var imageIds = exerciseImageLinks.stream()
                .map(ExerciseImageEntity::getImageId)
                .toList();

        if (imageIds.isEmpty()) {
            return List.of();
        }

        return imageJpaRepository.findByIdIn(imageIds).stream()
                .map(imageJpaMapper::toDomain)
                .toList();
    }

    @Override
    public Map<UUID, List<Image>> findImagesByExerciseIds(List<UUID> exerciseIds) {
        if (exerciseIds.isEmpty()) {
            return Map.of();
        }

        var exerciseImageLinks = exerciseImageJpaRepository.findByExerciseIdIn(exerciseIds);
        var imageIds = exerciseImageLinks.stream()
                .map(ExerciseImageEntity::getImageId)
                .distinct()
                .toList();

        if (imageIds.isEmpty()) {
            return Map.of();
        }

        var imageEntities = imageJpaRepository.findByIdIn(imageIds);
        var imageMap = imageEntities.stream()
                .collect(Collectors.toMap(
                        ImageEntity::getId,
                        imageJpaMapper::toDomain
                ));

        return exerciseImageLinks.stream()
                .filter(link -> imageMap.containsKey(link.getImageId()))
                .collect(Collectors.groupingBy(
                        ExerciseImageEntity::getExerciseId,
                        Collectors.mapping(
                                link -> imageMap.get(link.getImageId()),
                                Collectors.toList()
                        )
                ));
    }
}