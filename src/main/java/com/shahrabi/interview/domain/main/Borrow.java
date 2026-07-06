package com.shahrabi.interview.domain.main;

import com.shahrabi.interview.domain.AbstractAuditingEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString
@Entity
@Table(name = "borrow_book", schema = "core")
@EntityListeners(AuditingEntityListener.class)
public class Borrow extends AbstractAuditingEntity<UUID> {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "book_id", insertable = false, updatable = false)
    private Book book;

    @Column(name = "book_id", nullable = false)
    private UUID bookId;

    @Column(name="borrower_name", nullable = false, length = 32)
    private String borrowerName;

    @Column(name = "borrow_date", nullable = false, columnDefinition = "TIMESTAMP(3) WITH TIME ZONE")
    private LocalDateTime borrowDate;

    @Column(name = "return_date", columnDefinition = "TIMESTAMP(3) WITH TIME ZONE")
    private LocalDateTime returnDate;
}
