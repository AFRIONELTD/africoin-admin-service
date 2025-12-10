package com.afrione.africoinservice.infrastructure.persistence.daoimpl;

import com.afrione.africoinservice.domain.dao.UserPasswordHistoryEntityDao;
import com.afrione.africoinservice.domain.entities.UserPasswordHistoryEntity;
import com.afrione.africoinservice.infrastructure.persistence.repository.UserPasswordHistoryRepository;
import org.springframework.stereotype.Service;

@Service
public class UserPasswordHistoryEntityDaoImpl extends CrudDaoImpl<UserPasswordHistoryEntity, Long> implements UserPasswordHistoryEntityDao {

    private final UserPasswordHistoryRepository repository;

    public UserPasswordHistoryEntityDaoImpl(UserPasswordHistoryRepository repository) {
        super(repository);
        this.repository = repository;
    }
}
