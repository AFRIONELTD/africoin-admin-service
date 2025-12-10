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
    private final SequenceGenerator sequenceGenerator;
    public AppUserEntityDaoImpl(AppUserRepository repository, SequenceGenerator sequenceGenerator) {
        super(repository);
        this.repository = repository;
        this.sequenceGenerator = sequenceGenerator;
    }

    @Override
    public Optional<AppUserEntity> findRecordByUserId(String userId) {
        return repository.findTopByUserId(userId);
    }

    @Override
    public String generateUserId() {
        String userId = sequenceGenerator.generateID(2, 6);
        while(repository.existsByUserId(userId)) {
            userId = sequenceGenerator.generateID(2, 6);
        }
        return userId;
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
}
