package com.afrione.africoinservice.infrastructure.web.controllers;

import com.afrione.africoinservice.domain.entities.enums.PrivilegeTypeConstant;
import com.afrione.africoinservice.domain.services.PrivilegeValidationService;
import com.afrione.africoinservice.infrastructure.web.models.ApiResponseJSON;
import com.afrione.africoinservice.usecases.RoleUseCases;
import com.afrione.africoinservice.usecases.data.request.CreateRoleRequest;
import com.afrione.africoinservice.usecases.data.request.UpdateRoleRequest;
import com.afrione.africoinservice.usecases.data.request.AssignUserRoleRequest;
import com.afrione.africoinservice.usecases.data.response.role.RoleResponse;
import com.afrione.africoinservice.usecases.data.response.role.PrivilegeResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping(value = "/api/v1/roles", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Validated
@Transactional
@Tag(name = "Role Management", description = "APIs for managing roles and privileges")
public class RoleController {

    private final RoleUseCases roleUseCases;
    private final PrivilegeValidationService privilegeValidationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new role", description = "Create a new role with specified privileges")
    public ApiResponseJSON<RoleResponse> createRole(@RequestBody @Valid CreateRoleRequestJSON request) {
        RoleResponse response = roleUseCases.createRole(request.toRequest());
        return new ApiResponseJSON<>("Role created successfully", response);
    }

    @PutMapping("/{roleId}")
    @Operation(summary = "Update a role", description = "Update an existing role")
    public ApiResponseJSON<RoleResponse> updateRole(@PathVariable Long roleId,
                                                   @RequestBody @Valid UpdateRoleRequestJSON request) {
        RoleResponse response = roleUseCases.updateRole(roleId, request.toRequest());
        return new ApiResponseJSON<>("Role updated successfully", response);
    }

    @GetMapping("/{roleId}")
    @Operation(summary = "Get role by ID", description = "Get a specific role with its privileges")
    public ApiResponseJSON<RoleResponse> getRole(@PathVariable Long roleId) {
        RoleResponse response = roleUseCases.getRole(roleId);
        return new ApiResponseJSON<>("Role retrieved successfully", response);
    }

    @GetMapping
    @Operation(summary = "Get all roles", description = "Get all active roles with their privileges")
    public ApiResponseJSON<List<RoleResponse>> getAllRoles() {
        List<RoleResponse> response = roleUseCases.getAllRoles();
        return new ApiResponseJSON<>("Roles retrieved successfully", response);
    }

    @DeleteMapping("/{roleId}")
    @Operation(summary = "Delete a role", description = "Soft delete a role")
    public ApiResponseJSON<String> deleteRole(@PathVariable Long roleId) {
        roleUseCases.deleteRole(roleId);
        return new ApiResponseJSON<>("Role deleted successfully", "Role has been deleted");
    }

    @GetMapping("/privileges")
    @Operation(summary = "Get all privileges", description = "Get all available privileges in the system")
    public ApiResponseJSON<List<PrivilegeResponse>> getAllPrivileges() {
        List<PrivilegeResponse> response = roleUseCases.getAllPrivileges();
        return new ApiResponseJSON<>("Privileges retrieved successfully", response);
    }

    @PostMapping("/users/{userId}/assign")
    @Operation(summary = "Assign roles to user", description = "Assign one or more roles to a specific user")
    public ApiResponseJSON<String> assignRolesToUser(@PathVariable Long userId,
                                                    @RequestBody @Valid AssignUserRoleRequestJSON request) {
        roleUseCases.assignRolesToUser(userId, request.toRequest());
        return new ApiResponseJSON<>("Roles assigned successfully", "User roles have been updated");
    }

    @GetMapping("/users/{userId}")
    @Operation(summary = "Get user roles", description = "Get all roles assigned to a specific user")
    public ApiResponseJSON<List<RoleResponse>> getUserRoles(@PathVariable Long userId) {
        List<RoleResponse> response = roleUseCases.getUserRoles(userId);
        return new ApiResponseJSON<>("User roles retrieved successfully", response);
    }

    // Privilege Validation Endpoints
    @GetMapping("/privileges/validate")
    @Operation(summary = "Validate privilege consistency", description = "Check if all enum constants have corresponding database entities")
    public ApiResponseJSON<Boolean> validatePrivilegeConsistency() {
        boolean isConsistent = privilegeValidationService.validatePrivilegeConsistency();
        String message = isConsistent ? "All privileges are consistent" : "Some privileges are missing or inconsistent";
        return new ApiResponseJSON<>(message, isConsistent);
    }

    @GetMapping("/privileges/missing")
    @Operation(summary = "Get missing privileges", description = "Get enum constants that are missing from database")
    public ApiResponseJSON<Set<PrivilegeTypeConstant>> getMissingPrivileges() {
        Set<PrivilegeTypeConstant> missingPrivileges = privilegeValidationService.getMissingPrivileges();
        return new ApiResponseJSON<>("Missing privileges retrieved", missingPrivileges);
    }

    @GetMapping("/privileges/orphaned")
    @Operation(summary = "Get orphaned privileges", description = "Get database privileges that don't have corresponding enum constants")
    public ApiResponseJSON<Set<PrivilegeTypeConstant>> getOrphanedPrivileges() {
        Set<PrivilegeTypeConstant> orphanedPrivileges = privilegeValidationService.getOrphanedPrivileges();
        return new ApiResponseJSON<>("Orphaned privileges retrieved", orphanedPrivileges);
    }

    @PostMapping("/privileges/synchronize")
    @Operation(summary = "Synchronize privileges", description = "Create missing privileges and optionally remove orphaned ones")
    public ApiResponseJSON<Integer> synchronizePrivileges(@RequestParam(defaultValue = "false") boolean removeOrphaned) {
        int changesCount = privilegeValidationService.synchronizePrivileges(removeOrphaned);
        String message = changesCount > 0 ?
                "Privileges synchronized successfully. Changes made: " + changesCount :
                "No changes needed. All privileges are already synchronized.";
        return new ApiResponseJSON<>(message, changesCount);
    }

    // Request DTOs
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateRoleRequestJSON {
        @NotNull(message = "Role name cannot be null")
        private String roleName;

        @NotEmpty(message = "At least one privilege must be assigned")
        private List<Long> privilegeIds;

        public CreateRoleRequest toRequest() {
            return new CreateRoleRequest(roleName, privilegeIds);
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateRoleRequestJSON {
        private String roleName;
        private List<Long> privilegeIds;

        public UpdateRoleRequest toRequest() {
            return new UpdateRoleRequest(roleName, privilegeIds);
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AssignUserRoleRequestJSON {
        @NotEmpty(message = "At least one role must be assigned")
        private List<Long> roleIds;

        public AssignUserRoleRequest toRequest() {
            return new AssignUserRoleRequest(roleIds);
        }
    }
}
