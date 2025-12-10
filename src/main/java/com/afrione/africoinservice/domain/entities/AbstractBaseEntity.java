package com.afrione.africoinservice.domain.entities;

import com.afrione.africoinservice.domain.entities.enums.RecordStatusConstant;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.Objects;


@Getter
@Setter
@ToString
@MappedSuperclass
public abstract class AbstractBaseEntity<T extends Serializable> implements Serializable {

    @Serial
    private static final long serialVersionUID = -5554308939380869754L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(updatable = false)
    private T id;

    @Column(nullable = false)
    private OffsetDateTime dateCreated;

    @Column(nullable = false)
    private OffsetDateTime dateModified;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private RecordStatusConstant recordStatus = RecordStatusConstant.ACTIVE;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractBaseEntity<?> that = (AbstractBaseEntity<?>) o;
        return Objects.equals(id, that.id);
    }

    @PrePersist
    @PreUpdate
    public void prePersist() {
        if (dateCreated == null) {
            dateCreated = OffsetDateTime.now();
        }
        dateModified = OffsetDateTime.now();
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
