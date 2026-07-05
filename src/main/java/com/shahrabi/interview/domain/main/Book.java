package com.shahrabi.interview.domain.main;

import com.shahrabi.interview.domain.AbstractAuditingEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Table;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString
@Entity
@Table(name = "book", schema = "core")
@EntityListeners(AuditingEntityListener.class)
public class Book extends AbstractAuditingEntity<UUID> {

    @Column(name = "title", nullable = false, length = 128)
    private String title;

    @Column(name = "author_name", nullable = false, length = 32)
    private String authorName;

    // https://en.wikipedia.org/wiki/ISBN
    @Column(name = "ISBN", nullable = false, unique = true, updatable = false, length = 20)
    private String isbn;

    @Column(name = "published_year", nullable = false)
    private Integer publishYear;

    @Column(name = "is_active", nullable = false)
    private Boolean isAvailable = true;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;
}
