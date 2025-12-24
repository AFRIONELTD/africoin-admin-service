package com.afrione.africoinservice.usecases.data.response.customer;

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
    private String remoteUrl;
    private String fileType;
    private Long verificationId;
    private String status;
}