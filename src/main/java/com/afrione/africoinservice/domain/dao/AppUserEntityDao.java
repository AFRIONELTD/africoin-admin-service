package com.afrione.africoinservice.domain.dao;

import com.afrione.africoinservice.domain.entities.AppUserEntity;
import org.springframework.data.domain.Page;

import java.util.Optional;


public interface AppUserEntityDao extends CrudDao<AppUserEntity, Long> {
    boolean recordExistWithEmail(String email);
    Optional<AppUserEntity> findRecordByPhoneNumber(String phoneNumber);
    Optional<AppUserEntity> findRecordByEmail(String email);

    Page<AppUserEntity> findAllUsers(int pageNo, int pageSize);
}
