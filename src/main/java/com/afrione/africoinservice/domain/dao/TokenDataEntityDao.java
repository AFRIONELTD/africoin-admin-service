package com.afrione.africoinservice.domain.dao;

import com.afrione.africoinservice.domain.entities.TokenDataEntity;

import java.util.Optional;

public interface TokenDataEntityDao {
    Optional<TokenDataEntity> findByTokenId(String tokenId);
    void deleteExpiredRecords();
    TokenDataEntity saveRecord(TokenDataEntity tokenDataEntity);
}
