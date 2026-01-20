package com.afrione.africoinservice.usecases.data.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateRoleRequest {

    private String roleName;

    private List<Long> privilegeIds;
}
