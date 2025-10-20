package com.arturfrimu.lifemanager.shared;

import io.minio.GetObjectArgs;
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
    public UploadedFileInfo uploadFile(String folder, String filename, InputStream stream, long size, String fileContentType) {
        try {
            var objectName = "%s/%s".formatted(folder, filename);

            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(minioProperties.getBucket())
                            .object(objectName)
                            .stream(stream, size, -1)
                            .contentType(fileContentType)
                            .build()
            );

            log.info("Successfully uploaded file: {} to MinIO", objectName);
            
            return UploadedFileInfo.builder()
                    .presignedUrl(objectName)
                    .build();
        } catch (Exception e) {
            log.error("Failed to upload file: {} to MinIO", filename, e);
            throw new RuntimeException("Failed to upload file to storage", e);
        }
    }

    @Override
    public List<UploadedFileInfo> uploadFiles(String folder, List<MultipartFile> files) {
        List<UploadedFileInfo> uploadedFiles = new ArrayList<>();

        for (MultipartFile file : files) {
            try {
                var uniqueFilename = "%s_%s".formatted(UUID.randomUUID(), file.getOriginalFilename());
                var uploadedFileInfo = uploadFile(folder, uniqueFilename, file.getInputStream(), file.getSize(), file.getContentType());
                uploadedFiles.add(uploadedFileInfo);
            } catch (Exception e) {
                log.error("Failed to upload file: {}", file.getOriginalFilename(), e);
                throw new RuntimeException("Failed to upload file to storage", e);
            }
        }

        log.info("Successfully uploaded {} files to MinIO", uploadedFiles.size());
        return uploadedFiles;
    }

    @Override
    public void deleteFile(String objectName) {
        try {
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(minioProperties.getBucket())
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
        return "%s/%s%s".formatted(
                minioProperties.getEndpoint(),
                minioProperties.getBucket(),
                objectName
        );
    }

    @Override
    public byte[] downloadFile(String objectKey) {
        try (var stream = minioClient.getObject(
                GetObjectArgs.builder()
                        .bucket(minioProperties.getBucket())
                        .object(objectKey)
                        .build()
        )) {
            log.info("Downloading file from MinIO: {}", objectKey);
            return stream.readAllBytes();
        } catch (Exception e) {
            log.error("Failed to download file: {} from MinIO", objectKey, e);
            throw new RuntimeException("Failed to download file from storage", e);
        }
    }
}

