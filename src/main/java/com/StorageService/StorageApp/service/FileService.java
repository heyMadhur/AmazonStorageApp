package com.StorageService.StorageApp.service;

import com.StorageService.StorageApp.dto.FileSearchResponse;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.util.List;

public interface FileService {
    List<FileSearchResponse> searchFiles(String userName, String searchTerm);
    Resource downloadFile(String username, String fileName) throws FileNotFoundException;
    void uploadFile(String userName, MultipartFile file);
}
