package com.arturfrimu.lifemanager.shared;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "error-storage")
public class ErrorStorageProperties {
    String filePath;
}

