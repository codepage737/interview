package com.shahrabi.interview.service.main.specification;

import com.shahrabi.interview.domain.main.Book;
import com.shahrabi.interview.domain.main.Loan;
import com.shahrabi.interview.service.main.dto.LoanDto;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class LoanSpecification {

    public Specification<Loan> getAllSpecifications(LoanDto.QueryLoanDto dto) {
        return Specification
                .where(hasBorrowerName(dto.getBorrowerName()))
                .and(hasIsbn(dto.getIsbn()))
                .and(hasTitle(dto.getTitle()))
                .and(borrowedDateBetween(dto.getLoanDateStart(), dto.getLoanDateEnd()))
                .and(returnDateBetween(dto.getReturnDateStart(), dto.getReturnDateEnd()));
    }

    public Specification<Loan> hasBorrowerName(String borrowerName) {
        return (root, query, cb) -> {
            if (borrowerName != null) {
                return cb.equal(root.get("borrowerName"), borrowerName);
            }
            return cb.conjunction();
        };
    }

    public Specification<Loan> hasIsbn(String isbn) {
        return (root, query, cb) -> {
            if (isbn == null) return cb.conjunction();

            assert query != null;
            return cb.equal(root.get("book").get("isbn"), isbn);
        };
    }

    public Specification<Loan> hasTitle(String title) {
        return (root, query, cb) -> {
            if (title == null) return cb.conjunction();

            assert query != null;
            return cb.like(root.get("book").get("title"), "%" + title + "%");
        };
    }

    public Specification<Loan> borrowedDateBetween(LocalDateTime loanDateStart, LocalDateTime loanDateEnd) {
        return (root, query, cb) -> {
            if (loanDateStart != null && loanDateEnd != null) {
                return cb.between(root.get("loanDate"), loanDateStart, loanDateEnd);
            } else if (loanDateStart != null) {
                return cb.greaterThanOrEqualTo(root.get("loanDate"), loanDateStart);
            } else if (loanDateEnd != null) {
                return cb.lessThanOrEqualTo(root.get("loanDate"), loanDateEnd);
            }
            return cb.conjunction();
        };
    }

    public Specification<Loan> returnDateBetween(LocalDateTime returnDateStart, LocalDateTime returnDateEnd) {
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
