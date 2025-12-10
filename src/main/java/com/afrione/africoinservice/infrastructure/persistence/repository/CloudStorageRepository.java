package com.afrione.africoinservice.infrastructure.persistence.repository;

import com.afrione.africoinservice.domain.entities.CloudStorageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CloudStorageRepository extends JpaRepository<CloudStorageEntity, Long> {
}
