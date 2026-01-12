package com.afrione.africoinservice.infrastructure.web.controllers;

import com.afrione.africoinservice.infrastructure.security.AuthenticatedUser;
import com.afrione.africoinservice.infrastructure.web.models.ApiResponseJSON;
import com.afrione.africoinservice.usecases.SystemSettingsUseCase;
import com.afrione.africoinservice.usecases.data.response.PagedResponse;
import com.afrione.africoinservice.usecases.data.response.system_settings.SystemSettingsResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * Created by felixadewale on
 * 12/01/2026
 */

@Slf4j
@Validated
@Tag(name = "Settings Management Endpoints for ADMIN",  description = "Handles system settings operations")
@RestController
@RequestMapping(value = "/api/v1/admin/system-settings", produces = {MediaType.APPLICATION_JSON_VALUE})
@AllArgsConstructor
public class SystemSettingsController {

    private final SystemSettingsUseCase systemSettingsUseCase;

    @Operation(summary = "Update System setting value by id")
    @PutMapping(value = "/{system}/system-setting/{settingId}")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponseJSON<?> updateTransactionStatus(@AuthenticationPrincipal @Parameter(hidden = true) AuthenticatedUser authenticatedUser,
            @PathVariable @Pattern(regexp = "OTC|MERCHANT") @Parameter(description = "OTC|MERCHANT") String system, @PathVariable("settingId") Long settingId,
                                                      @RequestParam(value = "value") String value) {
        systemSettingsUseCase.updateSettings(authenticatedUser.getAccountId(), system, settingId, value);
        return new ApiResponseJSON<>("Record Updated Successfully");
    }

    @Operation(summary = "Returns paginated list of System Settings.")
    @GetMapping(value = "/{system}/system-settings", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ApiResponseJSON<PagedResponse<SystemSettingsResponse>> getSystemSettingsUseCase(
            @AuthenticationPrincipal @Parameter(hidden = true) AuthenticatedUser authenticatedUser,
            @PathVariable @Pattern(regexp = "OTC|MERCHANT") @Parameter(description = "OTC|MERCHANT") String system,
            @Parameter(description = "No. of records per page. Min:1, Max:20") @Valid @Min(value = 1) @Max(value = 500) @RequestParam(value = "size", defaultValue = "20") int size,
            @Parameter(description = "The index of the page to return. Min: 0") @Valid @Min(value = 0) @RequestParam(value = "page", defaultValue = "0") int page) {
        PagedResponse<SystemSettingsResponse> response = systemSettingsUseCase.getSystemSettings(authenticatedUser.getAccountId(), system, page, size);
        return new ApiResponseJSON<>("Settings returned successfully.", response);
    }
}
