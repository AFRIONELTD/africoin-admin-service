package com.afrione.africoinservice.infrastructure.web.controllers;


import com.afrione.africoinservice.infrastructure.web.models.ApiResponseJSON;

import com.afrione.africoinservice.usecases.CustomerReadUseCases;
import com.afrione.africoinservice.usecases.data.response.WalletCurrencyModel;
import com.afrione.africoinservice.usecases.data.response.merchant.CountryModel;
import com.afrione.africoinservice.usecases.data.response.otc.PayoutProcessorInfoModel;
import io.swagger.v3.oas.annotations.Operation;

import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/** Created by jnwanya on Wed, 10 Sept, 2025 */
@Validated
@Tag(
    name = "Master Record Endpoints",
    description = "Handles the static records of the application.")
@RestController
@RequestMapping(
    value = "/api/v1/common")
@RequiredArgsConstructor
public class MasterRecordController {

private final CustomerReadUseCases customerReadUseCases;

  @GetMapping("/supported-payout-corridors")
  @Operation(
      summary = "Get supported corridors for payout",
      description = "Get list of supported countries/corridors")
  public ResponseEntity<ApiResponseJSON<List<PayoutProcessorInfoModel>>> getSupportedCorridors() {
    List<PayoutProcessorInfoModel> corridors = customerReadUseCases.getSupportedCorridors();

    return new ResponseEntity<>(
        new ApiResponseJSON<>("Request processed successfully.", corridors), HttpStatus.OK);
  }

  @Operation(summary = "Return selected country")
  @GetMapping(value = "/selected-countries", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<ApiResponseJSON<List<CountryModel>>> getSelectedCountry() {
    List<CountryModel> responseList = customerReadUseCases.getActiveCountries();

    ApiResponseJSON<List<CountryModel>> apiResponseJSON = new ApiResponseJSON<>("Countries returned successfully.",responseList );
    return new ResponseEntity<>(apiResponseJSON, HttpStatus.OK);
  }


  @GetMapping(value = "/crypto-type", produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(summary = "Returns crypto type details.")
  public ResponseEntity<ApiResponseJSON<List<WalletCurrencyModel>>> getCryptoTypeDetails() {

    return ResponseEntity.ok(
        new ApiResponseJSON<>(
            "Crypto types returned successfully.",
                customerReadUseCases.getSupportedWalletCurrencies()));
  }



}
