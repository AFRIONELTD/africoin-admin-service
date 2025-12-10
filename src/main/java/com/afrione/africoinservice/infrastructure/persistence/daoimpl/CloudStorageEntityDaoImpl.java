package com.afrione.africoinservice.infrastructure.persistence.daoimpl;

import com.afrione.africoinservice.domain.dao.CloudStorageEntityDao;
import com.afrione.africoinservice.domain.entities.CloudStorageEntity;
import com.afrione.africoinservice.infrastructure.persistence.repository.CloudStorageRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class CloudStorageEntityDaoImpl extends CrudDaoImpl<CloudStorageEntity, Long> implements CloudStorageEntityDao {
    private final CloudStorageRepository repository;
    public CloudStorageEntityDaoImpl(CloudStorageRepository repository) {
        super(repository);
        this.repository = repository;
    }
}
