package com.afrione.africoinservice.domain.entities;

import com.afrione.africoinservice.domain.entities.enums.ApiLogTypeConstant;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;


@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "api_request_log")
public class ApiRequestLogEntity extends AbstractBaseEntity<Long> {

    public ApiRequestLogEntity(String requestPayload, ApiLogTypeConstant logType) {
        this.requestPayload = requestPayload;
        this.logType = logType.name();
    }

    private String requestReference;

    @Column(columnDefinition = "TEXT")
    private String requestPayload;

    @Column(columnDefinition = "TEXT")
    private String responsePayload;

    @Column(nullable = false)
    private String logType = ApiLogTypeConstant.GENERIC.name();

    private String requestUrl;

    private String provider;

    private long timeTakenInSeconds;

    private String httpMethod;
}
