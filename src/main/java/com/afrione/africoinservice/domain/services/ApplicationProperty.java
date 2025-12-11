package com.afrione.africoinservice.domain.services;

import org.springframework.stereotype.Service;

import java.util.Arrays;


@Service
public class ApplicationProperty {

    private final EnvironmentService environmentService;

    public ApplicationProperty(EnvironmentService environmentService) {
        this.environmentService = environmentService;
    }

    public boolean isProductionEnvironment() {
        return Arrays.stream(environmentService.getActiveProfiles()).anyMatch(evn -> evn.equalsIgnoreCase("prod"));
    }

    public int getAccessTokenExpiryTimeInMinutes() {
        return Integer.parseInt(environmentService.getVariable("token.expiry-time-in-minutes.access", "5"));
    }

    public int getRefreshTokenExpiryTimeInMinutes() {
        return Integer.parseInt(environmentService.getVariable("token.expiry-time-in-minutes.refresh"));
    }

    public String getClientKeyIOS() {
        return environmentService.getVariable("africoin.client-key.ios");
    }

    public String getClientKeyAndroid() {
        return environmentService.getVariable("africoin.client-key.android");
    }

    public String getClientKeyWeb() {
        return environmentService.getVariable("africoin.client-key.web");
    }

    public String getClientTokenSecretKey() {
        return environmentService.getVariable("TOKEN_CLIENT_SECRET_KEY", "");
    }

    public String getMerchantTokenSecretKey() {
        return environmentService.getVariable("TOKEN_B2B_SECRET_KEY", "");
    }

    public String getMsTokenSecretKey() {
        return environmentService.getVariable("TOKEN_MS_SECRET_KEY", "");
    }

    public String getBlockChainServiceUrl() {
        return environmentService.getVariable("AFRICOIN_BLOCKCHAIN_URL", "https://sandbox-blockchain-service.afrione.co");
    }

    public String getAFRERC20AdminPrivateKey() {
        return environmentService.getVariable("AFRICOIN_ERC20_ADMIN_PRIVATE_KEY", "e1d85a3d6f7c503850bc70ecb4de5391dbc7d391bce72bb88ed11e327b54a5e5");
    }

    public String getAFRTRC20AdminPrivateKey() {
        return environmentService.getVariable("AFRICOIN_TRC20_ADMIN_PRIVATE_KEY", "812f211c6e8b775e4e1fefffd389944c6328c77fc277c0af0155d7bbfe39b173");
    }

    public String getTestEmail() {
        return environmentService.getVariable("store-test-email", "+2347061635710:+2348052441734:+233267200000");
    }

    public String getAmazonS3AccessKey() {
        return environmentService.getVariable("AMAZON_KEY");
    }

    public String getAmazonS3SecretKey() {
        return environmentService.getVariable("AMAZON_SECRET");
    }

    public String getAmazonS3BucketName() {
        return environmentService.getVariable("AMAZON_S3_BUCKET", "africoin-sandbox");
    }

    public String getAmazonS3Region() {
        return environmentService.getVariable("amazon.s3.region", "eu-west-2");
    }

    public String getDojahSecretKey() {
        return environmentService.getVariable("DOJAH_SECRET_KEY", "");
    }

    public String getDojahAppId() {
        return environmentService.getVariable("DOJAH_APP_ID", "66f525861ceb127291bfda30");
    }


    public String getDojahBaseUrl() {
        return environmentService.getVariable("dojah.base-url", "https://sandbox.dojah.io");
    }

    public String getKorapayBaseUrl() {
        return environmentService.getVariable("korapay.base-url", "https://api.korapay.com");
    }

    public String getKorapaySecretKey() {
        return environmentService.getVariable("KORAPAY_SECRET_KEY", "sk_test_ELbk5rhyQakViFrNiGMg2RLV7yXvDsKUhZCYPdxy");
    }

    public String getKorapayPublicKey() {
        return environmentService.getVariable("KORAPAY_PUBLIC_KEY", "pk_test_1QdifYT7rCeE9WhPirMWFhwCNJ7DavkEXizZrRBB");
    }

    public String getCoinMarketCapApiKey() {
        return environmentService.getVariable("COINMARKET_API");
    }

    public String getTheTellerBaseUrl() {
        return environmentService.getVariable("THETHELLER_BASE_URL", "https://test.theteller.net");
    }

    public String getTheTellerMobileMoneyBaseUrl() {
        return environmentService.getVariable("THETHELLER_MM_BASE_URL", "https://test.theteller.net");
    }

    public String getApiBaseUrl() {
        return environmentService.getVariable("API_BASE_URL", "https://api-staging.afrione.co");
    }

    public String getTheTellerApiKey() {
        return environmentService.getVariable("THETHELLER_API_KEY", "");
    }

    public String getTheTellerMerchantId() {
        return environmentService.getVariable("THETHELLER_MERCHANT_ID", "");
    }

    public String getTheTellerUsername() {
        return environmentService.getVariable("THETHELLER_USERNAME", "");
    }

    public String getTheTellerPassCode() {
        return environmentService.getVariable("THETHELLER_PASSCODE", "0488");
    }

    public String getTheTellerSMSUsername() {
        return environmentService.getVariable("THETHELLER_SMS_USERNAME", "");
    }

    public String getTheTellerSMSPassword() {
        return environmentService.getVariable("THETHELLER_SMS_API_KEY", "");
    }


    public String getPaystackSecretKey() {
        return environmentService.getVariable("PAYSTACK_SECRET_KEY");
    }

    public String getPaystackBaseUrl() {
        return environmentService.getVariable("PAYSTACK_BASE_URL", "https://api.paystack.co");
    }

    public String getRegulatorReportingServiceUrl() {
        return environmentService.getVariable("regulator_reporting.service-url");
    }

    public String getRegulatorAuthUrl() {
        return environmentService.getVariable("regulator_reporting.auth-url");
    }

    public String getRegulatorClientId() {
        return environmentService.getVariable("BOG_REPORT_CLIENT_ID");
    }

    public String getRegulatorClientSecret() {
        return environmentService.getVariable("BOG_REPORT_CLIENT_KEY");
    }

    public String getGHSAccountNumber() {
        return environmentService.getVariable("ghs.account-number", "9044751292");

    }

    public String getGHSAccountName() {
        return environmentService.getVariable("ghs.account-name", "AFRICOIN LIMITED");

    }

    public String getGHSBankName() {
        return environmentService.getVariable("ghs.bank-name", "Bank of Ghana");
    }

    public String getAFRI_ERC20WalletAddress() {
        return environmentService.getVariable("OTC_WALLET_ADDRESS_AFRI_ERC20", "0xcAe20eb67e506FC340b50476d0AD3E56a9385C78");
    }

    public String getAFRI_TRC20WalletAddress() {
        return environmentService.getVariable("OTC_WALLET_ADDRESS_AFRI_TRC20", "TQb88ZbGhBx2x1hfPuVwkxgvW7NTGBdTtf");
    }

    public String getCymonzBaseUrl() {
        return environmentService.getVariable("coreapi.base-url");
    }

    public String getCymonzClientId() {
        return environmentService.getVariable("coreapi.client-id");
    }

    public String getCymonzClientSecret() {
        return environmentService.getVariable("coreapi.client-secret");
    }


    public String getPayAzaBaseUrl() {
        return environmentService.getVariable("PAYAZA_BASE_URL", "https://test.theteller.net");
    }

    public String getPayAzaPublicKey() {
        return environmentService.getVariable("PAYAZA_PUBLIC_KEY", "https://test.theteller.net");
    }

    public String getPayAzaNGNAccountReference() {
        return environmentService.getVariable("PAYAZA_NGN_ACCOUNT_REFERENCE", "https://api-staging.afrione.co");
    }

    public String getPayAzaGHSAccountReference() {
        return environmentService.getVariable("PAYAZA_GHS_ACCOUNT_REFERENCE", "");
    }

    public String getPayAzaMerchantTransactionPin() {
        return environmentService.getVariable("PAYAZA_MERCHANT_TRANSACTION_PIN", "");
    }

    public String getUniwalletBaseUrl() {
        return environmentService.getVariable("UNIWALLET_BASE_URL", "https://test.theteller.net");
    }

    public String getUniwalletApiKey() {
        return environmentService.getVariable("UNIWALLET_API_KEY", "https://test.theteller.net");
    }

    public String getUniwalletCreditProductId() {
        return environmentService.getVariable("UNIWALLET_CREDIT_PRODUCT_ID", "https://api-staging.afrione.co");
    }

    public String getUniwalletDebitProductId() {
        return environmentService.getVariable("UNIWALLET_DEBIT_PRODUCT_ID", "");
    }

    public String getUniwalletTransFlowId() {
        return environmentService.getVariable("UNIWALLET_TRANSFLOW_ID", "");
    }

    public String getB2BRequestClientKey() {
        return environmentService.getVariable("B2B_REQUEST_CLIENT_KEY", "8a26d5ddbb7ff954da3fefed3551088c");
    }

    public String merchangetServiceUrl() {
        return environmentService.getVariable("MERCHANT_SERVICE_URL", "https://api-merchant-staging.afrione.co");
    }
}
