package com.afrione.africoinservice.domain.entities;

import com.afrione.africoinservice.domain.entities.enums.ClientTypeConstant;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_password_history")
public class UserPasswordHistoryEntity extends AbstractBaseEntity<Long> {

    @JoinColumn(name = "user_id")
    @ManyToOne(optional = false)
    private AppUserEntity user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ClientTypeConstant deviceType;

    @Column(nullable = false)
    private String changedPassword;

    private String deviceModel;

    private String deviceId;

    private String requestIp;

    private String timeZone;
}
