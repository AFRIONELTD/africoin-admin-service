package com.afrione.africoinservice.infrastructure.persistence.repository;


import com.afrione.africoinservice.domain.entities.RoleEntity;
import com.afrione.africoinservice.domain.entities.enums.RecordStatusConstant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface RoleRepository extends JpaRepository<RoleEntity, Long> {

    Optional<RoleEntity> findByRoleNameIgnoreCase(String name);
    boolean existsByRoleNameIgnoreCase(String name);
    List<RoleEntity> getAllByRecordStatus(RecordStatusConstant recordStatus);

    @Query("SELECT r FROM RoleEntity r JOIN FETCH r.privileges WHERE r.id = :id")
    Optional<RoleEntity> findByIdWithPrivileges(@Param("id") Long id);

}
