package com.afrione.africoinservice.domain.dao;


import com.afrione.africoinservice.domain.entities.SessionDataEntity;
import com.afrione.africoinservice.domain.entities.enums.SessionDataTypeConstant;

import java.util.Optional;

/**
 * Created by jnwanya on
 * Sun, 31 Aug, 2025
 */
public interface SessionDataEntityDao extends CrudDao<SessionDataEntity, Long> {
    Optional<SessionDataEntity> findBySessionIdAndType(String sessionId, SessionDataTypeConstant sessionDataType);
    String generateSessionId();
    void deleteExpiredOrDeletedSessionData();
}
