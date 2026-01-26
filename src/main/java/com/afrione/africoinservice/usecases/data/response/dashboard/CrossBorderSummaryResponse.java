package com.afrione.africoinservice.usecases.data.response.dashboard;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by felixadewale on
 * 26/01/2026
 */
@Data
public class CrossBorderSummaryResponse {
    private List<CrossBorderSummaryData> data;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate endDate;
}
