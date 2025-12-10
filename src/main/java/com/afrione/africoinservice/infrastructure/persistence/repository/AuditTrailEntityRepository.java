package com.afrione.africoinservice.infrastructure.persistence.repository;

import com.afrione.africoinservice.domain.entities.AuditTrailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by jnya on
 * Mon, 08 Sept, 2025
 */
public interface AuditTrailEntityRepository extends JpaRepository<AuditTrailEntity, Long>, JpaSpecificationExecutor<AuditTrailEntity> {
}
