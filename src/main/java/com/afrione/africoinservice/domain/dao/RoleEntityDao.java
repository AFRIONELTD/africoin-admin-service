package com.afrione.africoinservice.domain.dao;


import com.afrione.africoinservice.domain.entities.RoleEntity;

import java.util.List;
import java.util.Optional;


public interface RoleEntityDao extends CrudDao<RoleEntity, Long> {
    Optional<RoleEntity> findByName(String name);
    RoleEntity getByName(String superUser);
    List<RoleEntity> getRecords();
    RoleEntity getRolesWithPrivileges(Long roleId);
}
