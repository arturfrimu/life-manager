package com.arturfrimu.lifemanager.domain.port.output;

import java.io.InputStream;
import java.time.Duration;
import java.util.List;
import java.util.Objects;

public interface FileStoragePort {
    void putObject(FileMetadata fileMetadata);

    List<String> listObjectKeys(String prefix);

    String presignGet(String objectKey, Duration ttl);

    record FileMetadata(
            String objectKey,
            InputStream stream,
            long size,
            String contentType
    ) {
        public static FileMetadata create(String objectKey, InputStream stream, long size, String contentType) {
            Objects.requireNonNull(objectKey, "ObjectKey could not be null");
            Objects.requireNonNull(stream, "Stream could not be null");
            Objects.requireNonNull(contentType, "ContentType could not be null");
            return new FileMetadata(objectKey, stream, size, contentType);
        }
    }
}
