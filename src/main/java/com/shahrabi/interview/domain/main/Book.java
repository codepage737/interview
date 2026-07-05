package com.shahrabi.interview.domain.main;

import com.shahrabi.interview.domain.AbstractAuditingEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.List;
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

    // https://en.wikipedia.org/wiki/ISBN
    @Column(name = "isbn", nullable = false, unique = true, updatable = false, length = 20)
    private String isbn;

    @Column(name = "title", nullable = false, length = 128)
    private String title;

    @Column(name = "author_name", nullable = false, length = 32)
    private String authorName;

    @Column(name = "published_year", nullable = false)
    private Integer publishYear;

    @Column(name = "is_available", nullable = false)
    @ColumnDefault("true")
    private Boolean isAvailable;

    @Column(name = "is_deleted", nullable = false)
    @ColumnDefault("false")
    private Boolean isDeleted;

    @OneToMany(mappedBy = "book", fetch = FetchType.LAZY)
    private List<Loan> loans;

    @Version
    private Long version;
}
