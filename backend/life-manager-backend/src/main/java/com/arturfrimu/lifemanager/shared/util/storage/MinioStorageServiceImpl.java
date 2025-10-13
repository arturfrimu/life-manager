package com.arturfrimu.lifemanager.shared.util.storage;

import com.arturfrimu.lifemanager.config.MinioProperties;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MinioStorageServiceImpl implements MinioStorageService {

    MinioClient minioClient;
    MinioProperties minioProperties;

    @Override
    public String uploadFile(String folder, String filename, InputStream stream, long size, String fileContentType) {
        try {
            var objectName = "%s/%s".formatted(folder, filename);

            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(minioProperties.getBucketName())
                            .object(objectName)
                            .stream(stream, size, -1)
                            .contentType(fileContentType)
                            .build()
            );

            var fileUrl = getFileUrl(objectName);
            log.info("Successfully uploaded file: {} to MinIO", objectName);
            return fileUrl;
        } catch (Exception e) {
            log.error("Failed to upload file: {} to MinIO", filename, e);
            throw new RuntimeException("Failed to upload file to storage", e);
        }
    }

    @Override
    public List<String> uploadFiles(String folder, List<MultipartFile> files) {
        List<String> uploadedFileUrls = new ArrayList<>();

        for (MultipartFile file : files) {
            try {
                var uniqueFilename = "%s_%s".formatted(UUID.randomUUID(), file.getOriginalFilename());
                var fileUrl = uploadFile(folder, uniqueFilename, file.getInputStream(), file.getSize(), file.getContentType());
                uploadedFileUrls.add(fileUrl);
            } catch (Exception e) {
                log.error("Failed to upload file: {}", file.getOriginalFilename(), e);
                throw new RuntimeException("Failed to upload file to storage", e);
            }
        }

        log.info("Successfully uploaded {} files to MinIO", uploadedFileUrls.size());
        return uploadedFileUrls;
    }

    @Override
    public void deleteFile(String objectName) {
        try {
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(minioProperties.getBucketName())
                            .object(objectName)
                            .build()
            );
            log.info("Successfully deleted file: {} from MinIO", objectName);
        } catch (Exception e) {
            log.error("Failed to delete file: {} from MinIO", objectName, e);
            throw new RuntimeException("Failed to delete file from storage", e);
        }
    }

    @Override
    public String getFileUrl(String objectName) {
        return "%s/%s/%s".formatted(
                minioProperties.getEndpoint(),
                minioProperties.getBucketName(),
                objectName
        );
    }
}

