package com.shahrabi.interview.service.main.specification;

import com.shahrabi.interview.domain.main.Borrow;
import com.shahrabi.interview.service.main.dto.BorrowDto;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class BorrowSpecification {

    public Specification<Borrow> getAllSpecifications(BorrowDto.QueryBorrowDto dto) {
        return Specification
                .where(hasBorrowerName(dto.getBorrowerName()))
                .and(hasIsbn(dto.getIsbn()))
                .and(hasTitle(dto.getTitle()))
                .and(borrowedDateBetween(dto.getBorrowDateStart(), dto.getBorrowDateEnd()))
                .and(returnDateBetween(dto.getReturnDateStart(), dto.getReturnDateEnd()));
    }

    public Specification<Borrow> hasBorrowerName(String borrowerName) {
        return (root, query, cb) -> {
            if (borrowerName != null) {
                return cb.equal(root.get("borrowerName"), borrowerName);
            }
            return cb.conjunction();
        };
    }

    public Specification<Borrow> hasIsbn(String isbn) {
        return (root, query, cb) -> {
            if (isbn == null) return cb.conjunction();

            assert query != null;
            return cb.equal(root.get("book").get("isbn"), isbn);
        };
    }

    public Specification<Borrow> hasTitle(String title) {
        return (root, query, cb) -> {
            if (title == null) return cb.conjunction();

            assert query != null;
            return cb.like(root.get("book").get("title"), "%" + title + "%");
        };
    }

    public Specification<Borrow> borrowedDateBetween(LocalDateTime borrowDateStart, LocalDateTime borrowDateEnd) {
        return (root, query, cb) -> {
            if (borrowDateStart != null && borrowDateEnd != null) {
                return cb.between(root.get("borrowDate"), borrowDateStart, borrowDateEnd);
            } else if (borrowDateStart != null) {
                return cb.greaterThanOrEqualTo(root.get("borrowDate"), borrowDateStart);
            } else if (borrowDateEnd != null) {
                return cb.lessThanOrEqualTo(root.get("borrowDate"), borrowDateEnd);
            }
            return cb.conjunction();
        };
    }

    public Specification<Borrow> returnDateBetween(LocalDateTime returnDateStart, LocalDateTime returnDateEnd) {
        return (root, query, cb) -> {
            if (returnDateStart != null && returnDateEnd != null) {
                return cb.between(root.get("returnDate"), returnDateStart, returnDateEnd);
            } else if (returnDateStart != null) {
                return cb.greaterThanOrEqualTo(root.get("returnDate"), returnDateStart);
            } else if (returnDateEnd != null) {
                return cb.lessThanOrEqualTo(root.get("returnDate"), returnDateEnd);
            }
            return cb.conjunction();
        };
    }
}
