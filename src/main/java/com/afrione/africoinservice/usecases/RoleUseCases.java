package com.afrione.africoinservice.usecases;

import com.afrione.africoinservice.usecases.data.request.CreateRoleRequest;
import com.afrione.africoinservice.usecases.data.request.UpdateRoleRequest;
import com.afrione.africoinservice.usecases.data.request.AssignUserRoleRequest;
import com.afrione.africoinservice.usecases.data.response.role.RoleResponse;
import com.afrione.africoinservice.usecases.data.response.role.PrivilegeResponse;

import java.util.List;

/**
 * Use cases for role management operations
 */
public interface RoleUseCases {

    /**
     * Create a new role
     */
    RoleResponse createRole(CreateRoleRequest request);

    /**
     * Update an existing role
     */
    RoleResponse updateRole(Long roleId, UpdateRoleRequest request);

    /**
     * Get role by ID
     */
    RoleResponse getRole(Long roleId);

    /**
     * Get all roles
     */
    List<RoleResponse> getAllRoles();

    /**
     * Delete a role
     */
    void deleteRole(Long roleId);

    /**
     * Get all privileges
     */
    List<PrivilegeResponse> getAllPrivileges();

    /**
     * Assign roles to user
     */
    void assignRolesToUser(Long userId, AssignUserRoleRequest request);

    /**
     * Get user roles
     */
    List<RoleResponse> getUserRoles(Long userId);
}
