package com.afrione.africoinservice.usecases.data.response.otc;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ExportResult {
    private String fileName;
    private String fileUrl;

}
