package com.afrione.africoinservice.usecases.data.response.dashboard;

import com.afrione.africoinservice.domain.entities.enums.GraphTypeConstant;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;


@Builder
@Data
public class OrderGraphResponse {
    private GraphTypeConstant graphType;
    private List<GraphData> afri_ERC20DataSet;
    private List<GraphData> afri_TRC20DataSet;
    private BigDecimal totalAFRi_ERC20Value;
    private BigDecimal totalAFRi_TRC20Value;
}
