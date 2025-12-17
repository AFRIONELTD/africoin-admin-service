package com.afrione.africoinservice.usecases.data.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;


@Data @NoArgsConstructor
public class PagedResponse<T> {

    private long totalRecords;
    private long totalPages;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private BigDecimal totalAmount;
    private List<T> records;

    public PagedResponse(long totalRecords, long totalPages, List<T> records) {
        this.totalRecords = totalRecords;
        this.totalPages = totalPages;
        this.records = records;
        this.totalAmount = BigDecimal.ZERO;
    }

    public PagedResponse(long totalRecords, long totalPages, BigDecimal totalAmount, List<T> records) {
        this.totalRecords = totalRecords;
        this.totalPages = totalPages;
        this.totalAmount = totalAmount;
        this.records = records;
    }
}
