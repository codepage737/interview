package com.shahrabi.interview.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

import static jakarta.persistence.GenerationType.AUTO;

@Data
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class AbstractAuditingEntity<T extends Serializable> implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = AUTO)
    private T id;

    @CreatedBy
    @Column(name="created_by", nullable = false, updatable = false)
    @JsonIgnore
    private UUID createdBy;

    @LastModifiedBy
    @Column(name="modified_by")
    @JsonIgnore
    private UUID modifiedBy;

    @CreatedDate
    @Column(name="created_at", updatable = false)
    @JsonIgnore
    private Instant createdAt = Instant.now();

    @LastModifiedDate
    @Column(name="updated_at")
    @JsonIgnore
    private Instant updatedAt = Instant.now();
}
