package com.afrione.africoinservice.domain.models.cloudservice;

import lombok.Data;

@Data
public class FileUploadRequest {
    private String fileName;
    private byte[] imageData;
    private String folderName;

    public FileUploadRequest(String fileName, byte[] imageData) {
        this.fileName = fileName;
        this.imageData = imageData;
    }
}
