package com.afrione.africoinservice.domain.dao;

import com.afrione.africoinservice.domain.entities.AppUserEntity;
import com.afrione.africoinservice.domain.entities.UserDeviceEntity;

import java.util.Optional;

public interface UserDeviceEntityDao extends CrudDao<UserDeviceEntity, Long>{
    Optional<UserDeviceEntity> findUserDevice(AppUserEntity appUserEntity);
    long countUserDevice(AppUserEntity appUserEntity);
    void deleteEarliestCreatedDevice(AppUserEntity appUserEntity);
}
