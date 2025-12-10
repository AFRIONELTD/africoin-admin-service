package com.afrione.africoinservice.domain.dao;

import com.afrione.africoinservice.domain.entities.ApiRequestLogEntity;
import com.afrione.africoinservice.domain.entities.enums.ApiLogTypeConstant;
import com.afrione.africoinservice.domain.models.RestClientResponse;

import java.time.OffsetDateTime;
import java.util.Optional;


public interface ApiRequestLogEntityDao extends CrudDao<ApiRequestLogEntity, Long> {

    ApiRequestLogEntity createLog(String url, String requestBody, ApiLogTypeConstant logType);
    ApiRequestLogEntity createLog(String requestId, String url, String requestBody, ApiLogTypeConstant logType);
    ApiRequestLogEntity createLog(String url, String requestBody);
    void deleteLog(String requestId, ApiLogTypeConstant logType);
    void updateLog(ApiRequestLogEntity restClientLogEntity, RestClientResponse clientResponse);
    void deleteLogBeforeDate(OffsetDateTime createdBeforeDate);
    Optional<ApiRequestLogEntity> findLogByLogTypeAndReference(ApiLogTypeConstant logType, String reference);
}
