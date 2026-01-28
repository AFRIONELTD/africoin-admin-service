package com.afrione.africoinservice.usecases.data.response.dashboard;

import com.afrione.africoinservice.domain.entities.enums.GraphTypeConstant;
import lombok.AllArgsConstructor;
import lombok.Data;


@AllArgsConstructor
@Data
public class GraphTypeResponse {
    GraphTypeConstant graphType;
    long duration;
}
