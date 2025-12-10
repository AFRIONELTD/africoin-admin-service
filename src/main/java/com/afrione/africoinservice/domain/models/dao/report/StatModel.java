package com.afrione.africoinservice.domain.models.dao.report;

import java.math.BigDecimal;
import java.time.LocalDateTime;


public interface StatModel {
    LocalDateTime getPeriod();
    String getType();
    Long getTotalCount();
    Long getRecordId();
    Long getUniqueTotalCount();
    BigDecimal getTotalAmount();
}
