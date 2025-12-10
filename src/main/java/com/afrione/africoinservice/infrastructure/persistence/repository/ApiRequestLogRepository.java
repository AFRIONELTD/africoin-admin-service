package com.afrione.africoinservice.infrastructure.persistence.repository;


import com.afrione.africoinservice.domain.entities.ApiRequestLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.OffsetDateTime;
import java.util.Optional;


public interface ApiRequestLogRepository extends JpaRepository<ApiRequestLogEntity, Long> {
    void deleteAllByDateCreatedBefore(OffsetDateTime createdBeforeDate);
    void deleteAllByRequestReferenceAndLogType(String reference, String logType);
    Optional<ApiRequestLogEntity> findTopByRequestReferenceAndLogType(String reference, String logType);
}
