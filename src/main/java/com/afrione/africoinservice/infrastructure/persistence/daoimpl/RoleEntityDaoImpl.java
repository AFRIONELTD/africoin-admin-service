package com.afrione.africoinservice.infrastructure.persistence.daoimpl;


import com.afrione.africoinservice.domain.dao.RoleEntityDao;
import com.afrione.africoinservice.domain.entities.RoleEntity;
import com.afrione.africoinservice.domain.entities.enums.RecordStatusConstant;
import com.afrione.africoinservice.infrastructure.persistence.repository.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class RoleEntityDaoImpl extends CrudDaoImpl<RoleEntity, Long>  implements RoleEntityDao {

    private final RoleRepository repository;
    public RoleEntityDaoImpl(RoleRepository repository) {
        super(repository);
        this.repository = repository;
    }

    @Override
    public Optional<RoleEntity> findByName(String name) {
        return repository.findByRoleNameIgnoreCase(name);
    }

    public boolean recordExistWithName(String name) {
        return repository.existsByRoleNameIgnoreCase(name);
    }

    @Override
    public RoleEntity getByName(String roleName) {
        return findByName(roleName).orElseThrow(() -> new RuntimeException("Role not found - "+roleName));
    }

    @Override
    public List<RoleEntity> getRecords() {
        return repository.getAllByRecordStatus(RecordStatusConstant.ACTIVE);
    }

    @Override
    public RoleEntity getRolesWithPrivileges(Long roleId) {
        return repository.findByIdWithPrivileges(roleId).orElseThrow(() -> new RuntimeException("Role not found - "+roleId));
    }
}
