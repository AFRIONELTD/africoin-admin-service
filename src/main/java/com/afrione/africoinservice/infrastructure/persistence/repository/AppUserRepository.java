package com.afrione.africoinservice.infrastructure.persistence.repository;


import com.afrione.africoinservice.domain.entities.AppUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;


public interface AppUserRepository extends JpaRepository<AppUserEntity, Long> {
    boolean existsByEmail(String email);
    Optional<AppUserEntity> findByEmail(String email);
    Optional<AppUserEntity> findByPhoneNumber(String userId);

    @Query("SELECT u FROM AppUserEntity u JOIN FETCH u.roles WHERE u.id = :id")
    Optional<AppUserEntity> findByIdWithRoles(@Param("id") Long id);
}
