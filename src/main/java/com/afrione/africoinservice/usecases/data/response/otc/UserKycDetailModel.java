package com.afrione.africoinservice.usecases.data.response.otc;

import lombok.Builder;
import lombok.Data;

import java.util.Objects;

@Data
@Builder
public class UserKycDetailModel {

    private CustomerDetails customerDetails;

    private AddressDetails addressDetails;

    private IdSection idSection;

    private EddSection eddSection;

    private SelfieSection selfieSection;


    @Data
    @Builder
    public static class CustomerDetails {
        @Builder.Default
        private String fileType = "GENERAL";
        private Long  verificationDataId;
        private String userId;
        private String firstName;
        private String verificationStatus;
        private String ghanaCardNumber;
        private String ghanaCardVerificationStatus;
        private String lastName;
        private String email;
        private String phoneNumber;
        private String dateOfBirth;
        private String gender;
        private String country;
        private String province;
        private String idType;
        private String idNumber;
        private String bridgeCustomerId;
    }

    @Data
    @Builder
    public static class AddressDetails {
        private String country;
        private String province;
        private String city;
        private String street;
        private FileModel proofOfAddress;
        @Builder.Default
        private String fileType= "POA";
    }

    @Data
    @Builder
    public static class IdSection {
        @Builder.Default
        String fileType = "ID";
        private String idType;
        private String idNumber;
        private String expiryDate;
        private FileModel idFront;
        private FileModel idBack;

    }
    @Data
    @Builder
    public static class EddSection {
        private String accountPurpose;
        private String employmentStatus;
        private String occupation;
        private String sourceOfFunds;
        private String expectedMonthlySpend;
    }

    @Data
    @Builder
    public static class SelfieSection {
        @Builder.Default
        private String fileType = "SELFIE";
        private FileModel selfie;
    }


}
