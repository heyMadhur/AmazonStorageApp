package com.StorageService.StorageApp.controller;

import com.StorageService.StorageApp.dto.FileSearchResponse;
import com.StorageService.StorageApp.service.FileService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.util.List;

@RestController
@RequestMapping("api/files")
public class FileController {

    private FileService fileService;

    public FileController(FileService fileService) {
        this.fileService= fileService;
    }

    @GetMapping("/search")
    public ResponseEntity<List<FileSearchResponse>> searchFiles(
            @RequestParam String username,
            @RequestParam String searchTerm
    ) {
        List<FileSearchResponse> files= fileService.searchFiles(username, searchTerm);
        return ResponseEntity.ok(files);
    }

    @GetMapping("/download")
    public ResponseEntity<Resource> downloadFile(
            @RequestParam String username,
            @RequestParam String fileName
    ) throws FileNotFoundException {
        Resource resource= fileService.downloadFile(username, fileName);
        return ResponseEntity.ok()
                .header((HttpHeaders.CONTENT_DISPOSITION), "attatchment; filename=\""+fileName+"\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> uploadFile(
            @RequestParam String username,
            @RequestParam("file")MultipartFile file
    ) {
        fileService.uploadFile(username, file);
        return ResponseEntity.ok().build();
    }




}
