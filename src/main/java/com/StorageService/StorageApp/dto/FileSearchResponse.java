package com.StorageService.StorageApp.dto;

import java.time.Instant;

public class FileSearchResponse {
    private String fileName;
    private Instant lastModified;
    private Long size;

    public FileSearchResponse(String fileName, Instant lastModified, Long size) {
        this.fileName = fileName;
        this.lastModified = lastModified;
        this.size = size;
    }

    public FileSearchResponse() {}

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Instant getLastModified() {
        return lastModified;
    }

    public void setLastModified(Instant lastModified) {
        this.lastModified = lastModified;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }
}
