package com.afrione.africoinservice.infrastructure.persistence.repository;

import com.afrione.africoinservice.domain.entities.UserPasswordHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserPasswordHistoryRepository extends JpaRepository<UserPasswordHistoryEntity, Long> {
}
