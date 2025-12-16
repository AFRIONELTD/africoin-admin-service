package com.afrione.africoinservice.infrastructure.persistence.daoimpl;

import com.afrione.africoinservice.domain.dao.SessionDataEntityDao;
import com.afrione.africoinservice.domain.entities.SessionDataEntity;
import com.afrione.africoinservice.domain.entities.enums.RecordStatusConstant;
import com.afrione.africoinservice.domain.entities.enums.SessionDataTypeConstant;
import com.afrione.africoinservice.domain.services.SequenceGenerator;
import com.afrione.africoinservice.infrastructure.persistence.repository.SessionDataRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service

public class SessionDataEntityDaoImpl extends CrudDaoImpl<SessionDataEntity, Long> implements SessionDataEntityDao {

    private final SessionDataRepository repository;
    private final SequenceGenerator sequenceGenerator;
    public SessionDataEntityDaoImpl(SessionDataRepository repository, SequenceGenerator sequenceGenerator) {
        super(repository);
        this.repository = repository;
        this.sequenceGenerator = sequenceGenerator;
    }
    @Override
    public Optional<SessionDataEntity> findBySessionIdAndType(String sessionId, SessionDataTypeConstant sessionDataType) {
      return repository.findFirstBySessionIdAndSessionDataTypeAndRecordStatus(sessionId, sessionDataType.name(), RecordStatusConstant.ACTIVE);
    }

    @Override
    public String generateSessionId() {
       return sequenceGenerator.nextSequenceId();
    }

    @Transactional
    @Override
    public void deleteExpiredOrDeletedSessionData() {
        repository.deleteAllByRecordStatusOrExpiryTimeBefore(RecordStatusConstant.DELETED, LocalDateTime.now());
    }
}
