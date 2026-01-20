package com.afrione.africoinservice.usecases.impl;

import com.afrione.africoinservice.domain.dao.AppUserEntityDao;
import com.afrione.africoinservice.domain.dao.PrivilegeEntityDao;
import com.afrione.africoinservice.domain.dao.RoleEntityDao;
import com.afrione.africoinservice.domain.entities.AppUserEntity;
import com.afrione.africoinservice.domain.entities.PrivilegeEntity;
import com.afrione.africoinservice.domain.entities.RoleEntity;
import com.afrione.africoinservice.domain.entities.enums.RecordStatusConstant;
import com.afrione.africoinservice.usecases.RoleUseCases;
import com.afrione.africoinservice.usecases.data.request.CreateRoleRequest;
import com.afrione.africoinservice.usecases.data.request.UpdateRoleRequest;
import com.afrione.africoinservice.usecases.data.request.AssignUserRoleRequest;
import com.afrione.africoinservice.usecases.data.response.role.RoleResponse;
import com.afrione.africoinservice.usecases.data.response.role.PrivilegeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class RoleUseCasesImpl implements RoleUseCases {

    private final RoleEntityDao roleEntityDao;
    private final PrivilegeEntityDao privilegeEntityDao;
    private final AppUserEntityDao appUserEntityDao;

    @Override
    public RoleResponse createRole(CreateRoleRequest request) {
        // Check if role name already exists
        if (roleEntityDao.findByName(request.getRoleName()).isPresent()) {
            throw new RuntimeException("Role with name '" + request.getRoleName() + "' already exists");
        }

        // Get privileges
        List<PrivilegeEntity> privileges = request.getPrivilegeIds().stream()
                .map(privilegeEntityDao::getRecordById)
                .collect(Collectors.toList());

        // Create role
        RoleEntity role = new RoleEntity();
        role.setRoleName(request.getRoleName());
        role.setPrivileges(privileges);
        role.setRecordStatus(RecordStatusConstant.ACTIVE);

        RoleEntity savedRole = roleEntityDao.saveRecord(role);
        return mapToRoleResponse(savedRole);
    }

    @Override
    public RoleResponse updateRole(Long roleId, UpdateRoleRequest request) {
        RoleEntity role = roleEntityDao.getRecordById(roleId);

        if (request.getRoleName() != null && !request.getRoleName().trim().isEmpty()) {
            // Check if new name conflicts with existing role
            roleEntityDao.findByName(request.getRoleName())
                    .filter(existingRole -> !existingRole.getId().equals(roleId))
                    .ifPresent(existingRole -> {
                        throw new RuntimeException("Role with name '" + request.getRoleName() + "' already exists");
                    });
            role.setRoleName(request.getRoleName());
        }

        if (request.getPrivilegeIds() != null && !request.getPrivilegeIds().isEmpty()) {
            List<PrivilegeEntity> privileges = request.getPrivilegeIds().stream()
                    .map(privilegeEntityDao::getRecordById)
                    .collect(Collectors.toList());
            role.setPrivileges(privileges);
        }

        RoleEntity savedRole = roleEntityDao.saveRecord(role);
        return mapToRoleResponse(roleEntityDao.getRolesWithPrivileges(savedRole.getId()));
    }

    @Override
    public RoleResponse getRole(Long roleId) {
        RoleEntity role = roleEntityDao.getRolesWithPrivileges(roleId);
        return mapToRoleResponse(role);
    }

    @Override
    public List<RoleResponse> getAllRoles() {
        return roleEntityDao.getRecords().stream()
                .map(role -> mapToRoleResponse(roleEntityDao.getRolesWithPrivileges(role.getId())))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteRole(Long roleId) {
        RoleEntity role = roleEntityDao.getRecordById(roleId);
        role.setRecordStatus(RecordStatusConstant.DELETED);
        roleEntityDao.saveRecord(role);
    }

    @Override
    public List<PrivilegeResponse> getAllPrivileges() {
        return privilegeEntityDao.getRecords().stream()
                .map(this::mapToPrivilegeResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void assignRolesToUser(Long userId, AssignUserRoleRequest request) {
        AppUserEntity user = appUserEntityDao.getRecordById(userId);

        List<RoleEntity> roles = request.getRoleIds().stream()
                .map(roleEntityDao::getRecordById)
                .collect(Collectors.toList());

        user.setRoles(roles);
        appUserEntityDao.saveRecord(user);
    }

    @Override
    public List<RoleResponse> getUserRoles(Long userId) {
        AppUserEntity user = appUserEntityDao.getUserWithRoles(userId);
        return user.getRoles().stream()
                .filter(role -> role.getRecordStatus() == RecordStatusConstant.ACTIVE)
                .map(role -> mapToRoleResponse(roleEntityDao.getRolesWithPrivileges(role.getId())))
                .collect(Collectors.toList());
    }

    private RoleResponse mapToRoleResponse(RoleEntity role) {
        List<PrivilegeResponse> privilegeResponses = role.getPrivileges() != null ?
                role.getPrivileges().stream()
                        .map(this::mapToPrivilegeResponse)
                        .collect(Collectors.toList()) : List.of();

        return RoleResponse.builder()
                .id(role.getId())
                .roleName(role.getRoleName())
                .privileges(privilegeResponses)
                .dateCreated(role.getDateCreated().toLocalDate())
                .dateModified(role.getDateModified().toLocalDate())
                .build();
    }

    private PrivilegeResponse mapToPrivilegeResponse(PrivilegeEntity privilege) {
        return PrivilegeResponse.builder()
                .id(privilege.getId())
                .type(privilege.getType())
                .displayName(privilege.getType().getDisplayName())
                .description(privilege.getType().getDescription())
                .build();
    }
}
