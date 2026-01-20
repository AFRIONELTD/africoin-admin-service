package com.afrione.africoinservice.usecases.data.response.role;

import com.afrione.africoinservice.domain.entities.enums.PrivilegeTypeConstant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PrivilegeResponse {

    private Long id;
    private PrivilegeTypeConstant type;
    private String displayName;
    private String description;
}
