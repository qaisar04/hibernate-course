package kz.baltabayev.entity;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import kz.baltabayev.listener.AuditDatesListener;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.Instant;

@MappedSuperclass
@Getter
@Setter
@EntityListeners(value = {AuditDatesListener.class})
public abstract class AuditableEntity<T extends Serializable> implements BaseEntity<T> {
    private Instant createdAt;
    private String createdBy;

    private Instant updatedAt;
    private String updatedBy;
}
