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
@Table(name = "user_device")
public class UserDeviceEntity extends AbstractBaseEntity<Long> {

    @JoinColumn(name = "user_id")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private AppUserEntity user;

    @Column(nullable = false)
    private String uniqueId;

    private String notificationToken;

    private String deviceModel;

    private String deviceOSVersion;

    @Enumerated(EnumType.STRING)
    private ClientTypeConstant deviceType;

    private String timezone;

    private String ipAddress;
}
