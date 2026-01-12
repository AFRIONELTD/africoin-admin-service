package com.afrione.africoinservice.usecases;


import com.afrione.africoinservice.usecases.data.response.PagedResponse;
import com.afrione.africoinservice.usecases.data.response.system_settings.SystemSettingsResponse;

/**
 * Created by felixadewale on
 * 12/01/2026
 */
public interface SystemSettingsUseCase extends ExternalRequestUseCases{
    void updateSettings(Long accountId, String system, Long settingId, String value);

    PagedResponse<SystemSettingsResponse> getSystemSettings(Long accountId, String system, int page, int size);
}
