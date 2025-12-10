package com.afrione.africoinservice.domain.dao;

import com.afrione.africoinservice.domain.entities.AppUserEntity;
import java.util.Optional;


public interface AppUserEntityDao extends CrudDao<AppUserEntity, Long> {
    Optional<AppUserEntity> findRecordByUserId(String userId);
    String generateUserId();
    boolean recordExistWithEmail(String email);
    Optional<AppUserEntity> findRecordByPhoneNumber(String phoneNumber);
    Optional<AppUserEntity> findRecordByEmail(String email);
}
