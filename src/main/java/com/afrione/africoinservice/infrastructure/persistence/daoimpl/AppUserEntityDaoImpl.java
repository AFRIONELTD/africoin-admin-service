package com.afrione.africoinservice.infrastructure.persistence.daoimpl;


import com.afrione.africoinservice.domain.dao.AppUserEntityDao;
import com.afrione.africoinservice.domain.entities.AppUserEntity;
import com.afrione.africoinservice.domain.services.SequenceGenerator;
import com.afrione.africoinservice.infrastructure.persistence.repository.AppUserRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
    public Page<AppUserEntity> findAllUsers(int pageNo, int pageSize, String searchTerm, Long roleId) {
        PageRequest pageRequest = PageRequest.of(pageNo, pageSize, Sort.Direction.DESC, "dateCreated");
        if (StringUtils.isBlank(searchTerm) && roleId == null) {
            return repository.findAll(pageRequest);
        }
        String st = StringUtils.isBlank(searchTerm) ? null : searchTerm;
        return repository.findAllUsersFiltered(st, roleId, pageRequest);
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
