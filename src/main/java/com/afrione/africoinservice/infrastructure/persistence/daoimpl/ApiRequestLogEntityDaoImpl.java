package com.afrione.africoinservice.infrastructure.persistence.daoimpl;


import com.afrione.africoinservice.domain.dao.ApiRequestLogEntityDao;
import com.afrione.africoinservice.domain.entities.ApiRequestLogEntity;
import com.afrione.africoinservice.domain.entities.enums.ApiLogTypeConstant;
import com.afrione.africoinservice.domain.models.RestClientResponse;
import com.afrione.africoinservice.infrastructure.persistence.repository.ApiRequestLogRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.Optional;

/**
 * Created by jnya on
 * Wed, 04 Jun, 2025
 */
@Service
public class ApiRequestLogEntityDaoImpl extends CrudDaoImpl<ApiRequestLogEntity, Long> implements ApiRequestLogEntityDao {

    private final ApiRequestLogRepository repository;
    public ApiRequestLogEntityDaoImpl(ApiRequestLogRepository repository) {
        super(repository);
        this.repository = repository;
    }

    @Override
    public ApiRequestLogEntity createLog(String url, String requestBody, ApiLogTypeConstant logType) {
        return createLog(null, url, requestBody, logType);
    }

    @Override
    public ApiRequestLogEntity createLog(String requestId, String url, String requestBody, ApiLogTypeConstant logType) {
        ApiRequestLogEntity requestLogEntity = new ApiRequestLogEntity(requestBody, logType);
        requestLogEntity.setRequestUrl(url);
        requestLogEntity.setRequestReference(requestId);
        return saveRecord(requestLogEntity);
    }

    @Override
    public ApiRequestLogEntity createLog(String url, String requestBody) {
        return createLog(url, requestBody, ApiLogTypeConstant.GENERIC);
    }

    @Override
    public void updateLog(ApiRequestLogEntity restClientLogEntity, RestClientResponse clientResponse) {
        restClientLogEntity.setResponsePayload(clientResponse.getResponseBody());
        restClientLogEntity.setTimeTakenInSeconds(clientResponse.getTimeTakenInSeconds());
        saveRecord(restClientLogEntity);
    }

    @Transactional
    @Override
    public void deleteLogBeforeDate(OffsetDateTime createdBeforeDate) {
        repository.deleteAllByDateCreatedBefore(createdBeforeDate);
    }

    @Transactional
    @Override
    public void deleteLog(String requestId, ApiLogTypeConstant logType) {
        repository.deleteAllByRequestReferenceAndLogType(requestId, logType.name());
    }

    @Override
    public Optional<ApiRequestLogEntity> findLogByLogTypeAndReference(ApiLogTypeConstant logType, String reference) {
        return repository.findTopByRequestReferenceAndLogType(reference, logType.name());
    }
}
