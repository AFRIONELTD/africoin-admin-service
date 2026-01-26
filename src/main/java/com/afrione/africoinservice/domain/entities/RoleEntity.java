package com.afrione.africoinservice.domain.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "role")
public class RoleEntity extends AbstractBaseEntity<Long> {

    @Column(name = "name", unique = true)
    private String roleName;

    @JoinTable(
            name = "role_privilege",
            joinColumns = @JoinColumn(name = "role_fk"),
            inverseJoinColumns = @JoinColumn(name = "privilege_fk")
    ) @ManyToMany(fetch = FetchType.LAZY)
    private List<PrivilegeEntity> privileges;

    @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
    private Set<AppUserEntity> appUserEntityList = new HashSet<>();



}
