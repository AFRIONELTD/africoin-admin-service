package com.afrione.africoinservice.infrastructure.persistence.repository;


import com.afrione.africoinservice.domain.entities.AppUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface AppUserRepository extends JpaRepository<AppUserEntity, Long> {
    boolean existsByEmail(String email);
    Optional<AppUserEntity> findByEmail(String email);
    Optional<AppUserEntity> findByPhoneNumber(String userId);
}
