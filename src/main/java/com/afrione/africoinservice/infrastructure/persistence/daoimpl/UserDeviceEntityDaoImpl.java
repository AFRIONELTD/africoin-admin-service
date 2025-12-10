package com.afrione.africoinservice.infrastructure.persistence.daoimpl;

import com.afrione.africoinservice.domain.dao.UserDeviceEntityDao;
import com.afrione.africoinservice.domain.entities.AppUserEntity;
import com.afrione.africoinservice.domain.entities.UserDeviceEntity;
import com.afrione.africoinservice.domain.entities.enums.RecordStatusConstant;
import com.afrione.africoinservice.infrastructure.persistence.repository.UserDeviceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserDeviceEntityDaoImpl extends CrudDaoImpl<UserDeviceEntity, Long> implements UserDeviceEntityDao {

    private final UserDeviceRepository repository;
    public UserDeviceEntityDaoImpl(UserDeviceRepository repository) {
        super(repository);
        this.repository = repository;
    }

    @Override
    public Optional<UserDeviceEntity> findUserDevice(AppUserEntity appUserEntity) {
        return repository.findFirstByUserAndRecordStatusOrderByDateCreatedDesc(appUserEntity, RecordStatusConstant.ACTIVE);
    }

    @Override
    public long countUserDevice(AppUserEntity appUserEntity) {
        return repository.countAllByUserAndRecordStatus(appUserEntity, RecordStatusConstant.ACTIVE);
    }

    @Transactional
    @Override
    public void deleteEarliestCreatedDevice(AppUserEntity appUserEntity) {
        if (countUserDevice(appUserEntity) < 10) {
            return;
        }
        Optional<UserDeviceEntity> opt = repository.findFirstByUserOrderByDateCreatedAsc(appUserEntity);
        opt.ifPresent(repository::delete);
    }
}
