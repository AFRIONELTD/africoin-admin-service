package com.afrione.africoinservice.infrastructure.persistence.repository;

import com.afrione.africoinservice.domain.entities.AppUserEntity;
import com.afrione.africoinservice.domain.entities.UserDeviceEntity;
import com.afrione.africoinservice.domain.entities.enums.RecordStatusConstant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserDeviceRepository extends JpaRepository<UserDeviceEntity, Long> {
    Optional<UserDeviceEntity> findFirstByUserAndRecordStatusOrderByDateCreatedDesc(AppUserEntity appUserEntity, RecordStatusConstant statusConstant);
    long countAllByUserAndRecordStatus(AppUserEntity appUserEntity, RecordStatusConstant statusConstant);
    Optional<UserDeviceEntity> findFirstByUserOrderByDateCreatedAsc(AppUserEntity appUserEntity);
}
