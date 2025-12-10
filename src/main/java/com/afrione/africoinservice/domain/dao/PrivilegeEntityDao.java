package com.afrione.africoinservice.domain.dao;


import com.afrione.africoinservice.domain.entities.PrivilegeEntity;
import com.afrione.africoinservice.domain.entities.enums.PrivilegeTypeConstant;

import java.util.List;
import java.util.Optional;


public interface PrivilegeEntityDao extends CrudDao<PrivilegeEntity, Long> {
    Optional<PrivilegeEntity> findByType(PrivilegeTypeConstant name);
    PrivilegeEntity getByType(PrivilegeTypeConstant name);
    List<PrivilegeEntity> getRecords();

}
