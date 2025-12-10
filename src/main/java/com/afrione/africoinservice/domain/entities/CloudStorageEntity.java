package com.afrione.africoinservice.domain.entities;

import com.afrione.africoinservice.domain.entities.enums.CloudStorageProviderConstant;
import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Entity
@Getter
@Setter
@Builder
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "cloud_storage")
public class CloudStorageEntity extends AbstractBaseEntity<Long> {
    private String fileId;

    private String remoteUrl;

    @Column(nullable = false)
    private String filename;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CloudStorageProviderConstant provider;
}
