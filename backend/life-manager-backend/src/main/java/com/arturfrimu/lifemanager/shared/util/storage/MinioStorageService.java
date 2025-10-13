package com.arturfrimu.lifemanager.shared.util.storage;

import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

public interface MinioStorageService {
    String uploadFile(String folder, String filename, InputStream stream, long size, String fileContentType);
    
    List<String> uploadFiles(String folder, List<MultipartFile> files);
    
    void deleteFile(String objectName);
    
    String getFileUrl(String objectName);
}

