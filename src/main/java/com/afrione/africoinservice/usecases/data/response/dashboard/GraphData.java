package com.afrione.africoinservice.usecases.data.response.dashboard;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

@Data
@NoArgsConstructor
public class GraphData {

    private String time;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private BigDecimal totalAmount;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long totalRecord;

    public GraphData(OffsetDateTime time, Long totalRecord) {
        this.time = time.format(DateTimeFormatter.ISO_DATE_TIME);
        this.totalRecord = totalRecord == null ? 0 : totalRecord;
        this.totalAmount = BigDecimal.ZERO;
    }

    public GraphData(OffsetDateTime time, BigDecimal totalAmount) {
        this.time = time.format(DateTimeFormatter.ISO_DATE_TIME);
        this.totalAmount = totalAmount == null ? BigDecimal.ZERO : totalAmount;
    }

    public GraphData(LocalDate time, BigDecimal totalAmount) {
        this.time = time.format(DateTimeFormatter.ISO_DATE);
        this.totalAmount = totalAmount == null ? BigDecimal.ZERO : totalAmount;
    }

    public GraphData(LocalDateTime time, Long totalRecord, BigDecimal totalAmount) {
        this.time = time.format(DateTimeFormatter.ISO_DATE_TIME);
        this.totalAmount = totalAmount == null ? BigDecimal.ZERO : totalAmount;
    }

    public GraphData(LocalDateTime time) {
        this.time = time.format(DateTimeFormatter.ISO_DATE_TIME);
        this.totalAmount =  BigDecimal.ZERO;
    }
}
