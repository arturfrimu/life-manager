package com.arturfrimu.lifemanager.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "minio")
public class MinioProperties {
    String endpoint;
    String accessKey;
    String secretKey;
    String bucketName;
    boolean autoCreateBucket;
}

