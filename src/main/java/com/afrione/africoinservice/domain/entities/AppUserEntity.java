package com.afrione.africoinservice.domain.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "app_user")
public class AppUserEntity extends AbstractBaseEntity<Long> {

    private String firstName;

    private String lastName;

    private String gender;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(name = "auth_key", nullable = false)
    private String authenticationKey;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private AppUserEntity createdBy;

    @Builder.Default
    private boolean requiresPasswordChange  = true;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "app_user_role",
            joinColumns = @JoinColumn(name = "user_fk"),
            inverseJoinColumns = @JoinColumn(name = "role_fk")
    )
    private List<RoleEntity> roles = new ArrayList<>();

    @Builder.Default
    @Column(name = "failed_login_attempts")
    private int failedLoginAttempts = 0;

    @Column(name = "last_login_at")
    private OffsetDateTime lastLoginAt;

    @Builder.Default
    @Column(name = "two_fa_enabled")
    private boolean twoFAEnabled = true;

    @Builder.Default
    @Column(name = "two_fa_method")
    private String twoFAMethod = "OTP";

}
