package com.arturfrimu.lifemanager.application.service;

import com.arturfrimu.lifemanager.adapters.outbound.persistence.jpa.entity.ExerciseImageEntity;
import com.arturfrimu.lifemanager.adapters.outbound.persistence.jpa.entity.ImageEntity;
import com.arturfrimu.lifemanager.adapters.outbound.persistence.jpa.repository.ExerciseImageJpaRepository;
import com.arturfrimu.lifemanager.adapters.outbound.persistence.jpa.repository.ImageJpaRepository;
import com.arturfrimu.lifemanager.application.port.input.ExerciseServicePort;
import com.arturfrimu.lifemanager.application.port.output.ExerciseRepositoryPort;
import com.arturfrimu.lifemanager.application.port.output.ImageRepositoryPort;
import com.arturfrimu.lifemanager.domain.model.Exercise;
import com.arturfrimu.lifemanager.domain.model.Image;
import com.arturfrimu.lifemanager.domain.model.Type;
import com.arturfrimu.lifemanager.shared.util.storage.MinioStorageService;
import com.arturfrimu.lifemanager.shared.util.storage.UploadedFileInfo;
import com.google.common.base.Preconditions;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ExerciseServiceAdapter implements ExerciseServicePort {

    ImageRepositoryPort imageRepositoryPort;
    ExerciseRepositoryPort exerciseRepositoryPort;

    MinioStorageService minioStorageService;

    ImageJpaRepository imageJpaRepository;
    ExerciseImageJpaRepository exerciseImageJpaRepository;

    @Override
    @Transactional
    public Exercise createExercise(String name, Type type, String description) {
        Preconditions.checkArgument(Objects.nonNull(name), "Name cannot be null");
        Preconditions.checkArgument(Objects.nonNull(type), "Type cannot be null");

        log.info("Creating exercise with name: {}", name);

        if (exerciseRepositoryPort.existsByName(name)) {
            throw new IllegalArgumentException("Exercise with name '%s' already exists".formatted(name));
        }

        var exercise = Exercise.create(name, type, description);
        var savedExercise = exerciseRepositoryPort.save(exercise);

        log.info("Successfully created exercise with id: {}", savedExercise.id());
        return savedExercise;
    }

    @Override
    @Transactional(readOnly = true)
    public Exercise getExercise(UUID id) {
        Preconditions.checkArgument(Objects.nonNull(id), "Id cannot be null");

        log.info("Retrieving exercise with id: {}", id);
        var exercise = exerciseRepositoryPort.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Exercise with id '%s' not found".formatted(id)));
        
        return enrichExerciseWithImages(exercise);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Exercise> findAllExercises(Pageable pageable) {
        Preconditions.checkArgument(Objects.nonNull(pageable), "Pageable cannot be null");
        
        log.info("Retrieving exercises with page: {}, size: {}", pageable.getPageNumber(), pageable.getPageSize());
        var exercisePage = exerciseRepositoryPort.findAll(pageable);
        
        var exerciseIds = exercisePage.getContent().stream()
                .map(Exercise::id)
                .toList();
        
        var imagesMap = imageRepositoryPort.findImagesByExerciseIds(exerciseIds);
        
        var enrichedExercises = exercisePage.getContent().stream()
                .map(exercise -> enrichExerciseWithImages(exercise, imagesMap))
                .toList();
        
        return new PageImpl<>(enrichedExercises, pageable, exercisePage.getTotalElements());
    }

    @Override
    @Transactional
    public void deleteExercise(UUID id) {
        Preconditions.checkArgument(Objects.nonNull(id), "Id cannot be null");
        
        log.info("Deleting exercise with id: {}", id);
        
        if (exerciseRepositoryPort.findById(id).isEmpty()) {
            throw new IllegalArgumentException("Exercise with id '%s' not found".formatted(id));
        }
        
        exerciseRepositoryPort.deleteById(id);
        
        log.info("Successfully deleted exercise with id: {}", id);
    }

    @Override
    @Transactional
    public Exercise updateExercise(UUID id, String name, Type type, String description) {
        Preconditions.checkArgument(Objects.nonNull(id), "Id cannot be null");
        Preconditions.checkArgument(Objects.nonNull(name), "Name cannot be null");
        Preconditions.checkArgument(Objects.nonNull(type), "Type cannot be null");
        
        log.info("Updating exercise with id: {}", id);
        
        var existingExercise = exerciseRepositoryPort.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Exercise with id '%s' not found".formatted(id)));
        
        if (!existingExercise.name().equals(name) && exerciseRepositoryPort.existsByName(name)) {
            throw new IllegalArgumentException("Exercise with name '%s' already exists".formatted(name));
        }
        
        var updatedExercise = new Exercise(
                existingExercise.id(),
                name,
                type,
                description,
                List.of(),
                existingExercise.created(),
                Instant.now()
        );
        
        var savedExercise = exerciseRepositoryPort.update(updatedExercise);
        
        log.info("Successfully updated exercise with id: {}", savedExercise.id());
        return enrichExerciseWithImages(savedExercise);
    }

    @Override
    @Transactional
    public List<String> uploadExerciseImages(UUID exerciseId, List<MultipartFile> images) {
        Preconditions.checkArgument(Objects.nonNull(exerciseId), "Exercise id cannot be null");
        Preconditions.checkArgument(Objects.nonNull(images) && !images.isEmpty(), "Images list cannot be null or empty");
        
        log.info("Uploading {} images for exercise with id: {}", images.size(), exerciseId);

        exerciseRepositoryPort.findById(exerciseId)
                .orElseThrow(() -> new IllegalArgumentException("Exercise with id '%s' not found".formatted(exerciseId)));

        for (MultipartFile image : images) {
            validateImageFile(image);
        }
        
        var folder = "/sport/exercises/%s/images".formatted(exerciseId);
        var uploadedFiles = minioStorageService.uploadFiles(folder, images);
        
        var imageEntities = new ArrayList<ImageEntity>();
        var exerciseImageEntities = new ArrayList<ExerciseImageEntity>();
        
        for (var uploadedFile : uploadedFiles) {
            var imageId = UUID.randomUUID();

            var backendImageUrl = "/api/v1/images/%s".formatted(imageId);
            
            var imageEntity = ImageEntity.builder()
                    .id(imageId)
                    .fileName(uploadedFile.fileName())
                    .objectKey(uploadedFile.objectKey())
                    .bucketName(uploadedFile.bucketName())
                    .url(backendImageUrl)
                    .contentType(uploadedFile.contentType())
                    .size(uploadedFile.size())
                    .build();
            
            imageEntities.add(imageEntity);
            
            var exerciseImageEntity = ExerciseImageEntity.builder()
                    .exerciseId(exerciseId)
                    .imageId(imageId)
                    .build();
            
            exerciseImageEntities.add(exerciseImageEntity);
        }
        
        imageJpaRepository.saveAll(imageEntities);
        exerciseImageJpaRepository.saveAll(exerciseImageEntities);
        
        log.info("Successfully saved {} image entities and {} exercise-image links to database", 
                imageEntities.size(), exerciseImageEntities.size());
        
        var imageUrls = uploadedFiles.stream()
                .map(UploadedFileInfo::url)
                .toList();
        
        log.info("Successfully uploaded and saved {} images for exercise with id: {}", imageUrls.size(), exerciseId);
        return imageUrls;
    }
    
    private void validateImageFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File cannot be empty");
        }
        
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("File must be an image. Received content type: %s".formatted(contentType));
        }
        
        long maxFileSize = 10 * 1024 * 1024; // 10MB
        if (file.getSize() > maxFileSize) {
            throw new IllegalArgumentException("File size cannot exceed 10MB. Received: %d bytes".formatted(file.getSize()));
        }
    }

    private Exercise enrichExerciseWithImages(Exercise exercise) {
        var images = imageRepositoryPort.findImagesByExerciseId(exercise.id());
        return new Exercise(
                exercise.id(),
                exercise.name(),
                exercise.type(),
                exercise.description(),
                images,
                exercise.created(),
                exercise.updated()
        );
    }

    private Exercise enrichExerciseWithImages(Exercise exercise, Map<UUID, List<Image>> imagesMap) {
        var images = imagesMap.getOrDefault(exercise.id(), List.of());
        return new Exercise(
                exercise.id(),
                exercise.name(),
                exercise.type(),
                exercise.description(),
                images,
                exercise.created(),
                exercise.updated()
        );
    }
}

