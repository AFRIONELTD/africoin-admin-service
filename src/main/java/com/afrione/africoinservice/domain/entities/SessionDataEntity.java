package com.afrione.africoinservice.domain.entities;

import com.afrione.africoinservice.domain.entities.enums.ClientTypeConstant;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Created by jnwanya on
 * Sun, 31 Aug, 2025
 */
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "session_data")
public class SessionDataEntity extends AbstractBaseEntity<Long> {

    @Column(nullable = false)
    private String sessionId;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String payload;

    @Column(nullable = false)
    private String sessionDataType;

    @Column(nullable = false)
    private LocalDateTime expiryTime;
}
