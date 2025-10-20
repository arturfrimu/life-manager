package com.arturfrimu.lifemanager.shared.util.storage;

import lombok.Builder;

@Builder
public record UploadedFileInfo(
        String presignedUrl
) {}