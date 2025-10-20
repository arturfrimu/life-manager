package com.arturfrimu.lifemanager.shared;

import lombok.Builder;

@Builder
public record UploadedFileInfo(
        String presignedUrl
) {}
