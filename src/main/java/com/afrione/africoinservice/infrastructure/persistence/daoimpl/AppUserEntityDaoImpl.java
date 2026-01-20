package com.afrione.africoinservice.infrastructure.persistence.daoimpl;


import com.afrione.africoinservice.domain.dao.AppUserEntityDao;
import com.afrione.africoinservice.domain.entities.AppUserEntity;
import com.afrione.africoinservice.domain.services.SequenceGenerator;
import com.afrione.africoinservice.infrastructure.persistence.repository.AppUserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class AppUserEntityDaoImpl extends CrudDaoImpl<AppUserEntity, Long> implements AppUserEntityDao {

    private final AppUserRepository repository;
    public AppUserEntityDaoImpl(AppUserRepository repository) {
        super(repository);
        this.repository = repository;
    }

    @Override
    public Optional<AppUserEntity> findRecordByEmail(String email) {
        return repository.findByEmail(email.toLowerCase());
    }

    @Override
    public Optional<AppUserEntity> findRecordByPhoneNumber(String phoneNumber) {
        return repository.findByPhoneNumber(phoneNumber);
    }

    @Override
    public boolean recordExistWithEmail(String email) {
        return repository.existsByEmail(email.toLowerCase());
    }

    @Override
    public AppUserEntity getUserWithRoles(Long userId) {
        return repository.findByIdWithRoles(userId)
                .orElseThrow(() -> new RuntimeException("User not found - " + userId));
    }
}
