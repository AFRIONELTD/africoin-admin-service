package com.afrione.africoinservice.usecases.data.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateRoleRequest {

    @NotBlank(message = "Role name cannot be empty")
    private String roleName;

    @NotEmpty(message = "At least one privilege must be assigned")
    private List<Long> privilegeIds;
}
