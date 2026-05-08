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


    @Data
    @Builder
    public static class CustomerDetails {

        private String userId;
        private String firstName;
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
    }

    @Data
    @Builder
    public static class IdSection {
        private String idType;
        private String idNumber;
        private String expiryDate;
        private FileModel idFront;
        private FileModel idBack;
        private FileModel selfie;
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



}
