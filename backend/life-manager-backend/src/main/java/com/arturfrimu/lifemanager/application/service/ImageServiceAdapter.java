package com.arturfrimu.lifemanager.application.service;

import com.arturfrimu.lifemanager.adapters.outbound.persistence.jpa.entity.ExerciseImageEntity;
import com.arturfrimu.lifemanager.adapters.outbound.persistence.jpa.repository.ExerciseImageJpaRepository;
import com.arturfrimu.lifemanager.adapters.outbound.persistence.jpa.repository.ImageJpaRepository;
import com.arturfrimu.lifemanager.application.port.input.ImageServicePort;
import com.arturfrimu.lifemanager.shared.util.storage.MinioStorageService;
import com.google.common.base.Preconditions;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.util.Objects;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ImageServiceAdapter implements ImageServicePort {

    MinioStorageService minioStorageService;

    ImageJpaRepository imageJpaRepository;
    ExerciseImageJpaRepository exerciseImageJpaRepository;

    @Override
    @Transactional(readOnly = true)
    public byte[] downloadExerciseImagesAsZip(UUID exerciseId) {
        Preconditions.checkArgument(Objects.nonNull(exerciseId), "Exercise id cannot be null");
        
        log.info("Downloading all images for exercise with id: {}", exerciseId);
        
        var exerciseImages = exerciseImageJpaRepository.findByExerciseId(exerciseId);
        
        if (exerciseImages.isEmpty()) {
            throw new IllegalArgumentException("No images found for exercise with id '%s'".formatted(exerciseId));
        }
        
        var imageIds = exerciseImages.stream()
                .map(ExerciseImageEntity::getImageId)
                .toList();
        
        var images = imageJpaRepository.findByIdIn(imageIds);
        
        try (var baos = new ByteArrayOutputStream(); var zos = new ZipOutputStream(baos)) {
            for (var image : images) {
                var imageData = minioStorageService.downloadFile(image.getObjectKey());
                
                var zipEntry = new ZipEntry(image.getFileName());
                zos.putNextEntry(zipEntry);
                zos.write(imageData);
                zos.closeEntry();
                
                log.info("Added image to ZIP: {}", image.getFileName());
            }
            
            zos.finish();
            var zipData = baos.toByteArray();
            
            log.info("Successfully created ZIP with {} images ({} bytes)", images.size(), zipData.length);
            return zipData;
            
        } catch (Exception e) {
            log.error("Failed to create ZIP for exercise: {}", exerciseId, e);
            throw new RuntimeException("Failed to create ZIP archive", e);
        }
    }
}
