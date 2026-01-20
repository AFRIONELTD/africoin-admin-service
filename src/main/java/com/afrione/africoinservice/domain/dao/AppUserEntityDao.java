package com.afrione.africoinservice.domain.dao;

import com.afrione.africoinservice.domain.entities.AppUserEntity;
import java.util.Optional;


public interface AppUserEntityDao extends CrudDao<AppUserEntity, Long> {
    boolean recordExistWithEmail(String email);
    Optional<AppUserEntity> findRecordByPhoneNumber(String phoneNumber);
    Optional<AppUserEntity> findRecordByEmail(String email);
    AppUserEntity getUserWithRoles(Long userId);
}
