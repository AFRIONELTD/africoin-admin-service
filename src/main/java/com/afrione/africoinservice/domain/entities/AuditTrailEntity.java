package com.afrione.africoinservice.domain.entities;

import com.afrione.africoinservice.domain.entities.enums.AuditActionTypeConstant;
import jakarta.persistence.*;
import lombok.*;

/**
 * Created by jnya on
 * Mon, 01 Sept, 2025
 */
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "audit_trail")
public class AuditTrailEntity extends AbstractBaseEntity<Long> {

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AuditActionTypeConstant actionType;

    private Long actorId;

    private Long accountId;

    private String actionUserName;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;


    private Long entityId;

    @Column(nullable = false)
    private String entityName;
}
