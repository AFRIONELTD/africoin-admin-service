package com.afrione.africoinservice.infrastructure.persistence.repository;


import com.afrione.africoinservice.domain.entities.SessionDataEntity;
import com.afrione.africoinservice.domain.entities.enums.RecordStatusConstant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface SessionDataRepository extends JpaRepository<SessionDataEntity, Long> {
    Optional<SessionDataEntity> findFirstBySessionIdAndSessionDataTypeAndRecordStatus(String sessionId, String dataType, RecordStatusConstant status);
    void deleteAllByRecordStatusOrExpiryTimeBefore(RecordStatusConstant statusConstant, LocalDateTime localDateTime);
}
