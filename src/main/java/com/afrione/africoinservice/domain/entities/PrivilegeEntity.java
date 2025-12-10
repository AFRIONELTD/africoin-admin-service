package com.afrione.africoinservice.domain.entities;

import com.afrione.africoinservice.domain.entities.enums.PrivilegeTypeConstant;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "privilege")
public class PrivilegeEntity extends AbstractBaseEntity<Long> {

    @Column(nullable = false, name = "privilege_type", unique = true)
    @Enumerated(EnumType.STRING)
    private PrivilegeTypeConstant type;

    @JsonIgnore
    @ManyToMany(mappedBy = "privileges", fetch = FetchType.LAZY)
    private List<RoleEntity> roles;

}
