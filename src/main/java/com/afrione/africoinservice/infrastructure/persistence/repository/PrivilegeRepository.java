package com.afrione.africoinservice.infrastructure.persistence.repository;


import com.afrione.africoinservice.domain.entities.PrivilegeEntity;
import com.afrione.africoinservice.domain.entities.enums.PrivilegeTypeConstant;
import com.afrione.africoinservice.domain.entities.enums.RecordStatusConstant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface PrivilegeRepository extends JpaRepository<PrivilegeEntity, Long> {
    Optional<PrivilegeEntity> findByType(PrivilegeTypeConstant name);
    List<PrivilegeEntity> getAllByRecordStatus(RecordStatusConstant status);
}
