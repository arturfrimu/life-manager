package com.arturfrimu.lifemanager.shared.util.storage;

import lombok.Builder;

@Builder
public record UploadedFileInfo(
        String fileName,
        String objectKey,
        String bucketName,
        String url,
        String contentType,
        Long size
) {}