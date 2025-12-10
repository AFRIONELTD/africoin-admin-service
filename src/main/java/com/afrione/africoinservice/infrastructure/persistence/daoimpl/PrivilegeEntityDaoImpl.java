package com.afrione.africoinservice.infrastructure.persistence.daoimpl;


import com.afrione.africoinservice.domain.dao.PrivilegeEntityDao;
import com.afrione.africoinservice.domain.entities.PrivilegeEntity;
import com.afrione.africoinservice.domain.entities.enums.PrivilegeTypeConstant;
import com.afrione.africoinservice.domain.entities.enums.RecordStatusConstant;
import com.afrione.africoinservice.infrastructure.persistence.repository.PrivilegeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class PrivilegeEntityDaoImpl extends CrudDaoImpl<PrivilegeEntity, Long> implements PrivilegeEntityDao {

    private final PrivilegeRepository repository;
    public PrivilegeEntityDaoImpl(PrivilegeRepository repository) {
        super(repository);
        this.repository = repository;
    }

    @Override
    public Optional<PrivilegeEntity> findByType(PrivilegeTypeConstant name) {
        return repository.findByType(name);
    }

    @Override
    public PrivilegeEntity getByType(PrivilegeTypeConstant name) {
        return findByType(name).orElseThrow(() -> new RuntimeException("No privilege with name " + name));
    }

    @Override
    public List<PrivilegeEntity> getRecords() {
        return repository.getAllByRecordStatus(RecordStatusConstant.ACTIVE);
    }
}
