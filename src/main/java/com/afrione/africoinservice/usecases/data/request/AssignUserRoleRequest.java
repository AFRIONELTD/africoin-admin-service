package com.afrione.africoinservice.usecases.data.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssignUserRoleRequest {

    @NotEmpty(message = "At least one role must be assigned")
    private List<Long> roleIds;
}
