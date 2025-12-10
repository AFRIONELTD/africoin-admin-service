package com.afrione.africoinservice.domain.services;

import com.afrione.africoinservice.domain.models.cloudservice.FileUploadRequest;
import com.afrione.africoinservice.domain.models.cloudservice.FileUploadResponse;

public interface CloudStorageService {
    FileUploadResponse processUpload(FileUploadRequest request);
}
