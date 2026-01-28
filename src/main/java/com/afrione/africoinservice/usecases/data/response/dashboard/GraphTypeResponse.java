package com.afrione.africoinservice.usecases.data.response.dashboard;

import io.swagger.v3.oas.annotations.Parameter;
import lombok.AllArgsConstructor;
import lombok.Data;


@AllArgsConstructor
@Data
public class GraphTypeResponse {
    @Parameter(description = "DAY|WEEK|MONTH")
    String graphType;
    long duration;
}
