package com.afrione.africoinservice.usecases.data.response.merchant;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AdminMerchantResponse {
    private Long id;
    private String merchantId;
    private String fullname;
    private String businessName;
    private String email;
    private String verificationStatus;
    private FileModel cacDocument;
    private FileModel statusReport;
    private FileModel moA;
    private DirectorDetailResponse director;
    String dateJoined;
    String country;

    public enum DetailLevel {BASIC, FULL}

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DirectorDetailResponse {
        private Long id;
        private String firstName;
        private String lastName;
        private String status;
        private String idType;
        private FileModel idCard;
    }
}

