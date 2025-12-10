package com.afrione.africoinservice.domain.models.dao.search;

import com.afrione.africoinservice.domain.entities.enums.AuditActionTypeConstant;
import lombok.Data;

import java.time.OffsetDateTime;

/**
 * Created by jnya on
 * Mon, 01 Sept, 2025
 */
@Data
public class AuditTrailSearchDTO {
    private OffsetDateTime fromTime;
    private OffsetDateTime toTime;
    private AuditActionTypeConstant actionType;
}
