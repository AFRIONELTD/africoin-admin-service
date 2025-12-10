package com.afrione.africoinservice.usecases.data.response.merchant;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FileModel {
    private String fileId;
    private String remoteUrl;
    private String filename;
    private String fileType;
    private long fileSize;
    private String status;
}