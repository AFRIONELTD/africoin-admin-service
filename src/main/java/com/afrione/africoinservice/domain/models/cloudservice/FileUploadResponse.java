package com.afrione.africoinservice.domain.models.cloudservice;

import lombok.Data;

@Data
public class FileUploadResponse {
    private String fileId;
    private String fileUrl;
    private boolean success;
    private long fileSizeInKB;
}
