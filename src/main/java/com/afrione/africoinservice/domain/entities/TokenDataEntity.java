package com.afrione.africoinservice.domain.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "token_data")
public class TokenDataEntity extends AbstractBaseEntity<Long>  {
    @Column(nullable = false, unique = true)
    private String tokenId;

    @Column(nullable = false)
    private String userId;

    private int reuseTimes;

    @Column(nullable = false)
    private LocalDateTime expiryTime;
}
