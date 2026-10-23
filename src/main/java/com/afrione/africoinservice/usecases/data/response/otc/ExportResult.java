package com.afrione.africoinservice.usecases.data.response.otc;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ExportResult {
    private String message;
    private String fileName;
    private String fileUrl;
    @Schema(description = "It has value between PROCESSING|COMPLETED. PROCESSING means the report will be sent to their email and for COMPLETED, the response " +
            "will return the link to download the file")
    private String status;

}
