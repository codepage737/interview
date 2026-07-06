package com.shahrabi.interview.service.main.specification;

import com.shahrabi.interview.domain.main.Book;
import com.shahrabi.interview.service.main.dto.BookDto;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class BookSpecification {

    public Specification<Book> getAllSpecifications(BookDto.QueryBookDto dto) {
        return Specification
                .where(hasId(dto.getId()))
                .and(hasIsbn(dto.getIsbn()))
                .and(hasTitle(dto.getTitle()))
                .and(hasAuthorName(dto.getAuthorName()))
                .and(hasPublishYear(dto.getPublishYear()))
                .and(hasPublishYearBetween(dto.getPublishYearStart(), dto.getPublishYearEnd()))
                .and(isAvailable(dto.getIsAvailable()));
//                .and(isDeleted(dto.getIsDeleted()));
    }

    public Specification<Book> hasId(UUID id) {
        return (root, query, cb) -> {
            if (id != null) {
                return cb.equal(root.get("id"), id);
            }
            return cb.conjunction();
        };
    }

    public Specification<Book> hasIsbn(String isbn) {
        return (root, query, cb) -> {
            if (isbn != null) {
                return cb.equal(root.get("isbn"), isbn);
            }
            return cb.conjunction();
        };
    }

    public Specification<Book> hasTitle(String title) {
        return (root, query, cb) -> {
            if (title != null) {
                return cb.like(root.get("title"), "%" + title + "%");
            }
            return cb.conjunction();
        };
    }

    public Specification<Book> hasAuthorName(String authorName) {
        return (root, query, cb) -> {
            if (authorName != null) {
                return cb.like(root.get("authorName"), "%" + authorName + "%");
            }
            return cb.conjunction();
        };
    }

    public Specification<Book> hasPublishYear(Integer publishYear) {
        return (root, query, cb) -> {
            if (publishYear != null) {
                return cb.equal(root.get("publishYear"), publishYear);
            }
            return cb.conjunction();
        };
    }

    public Specification<Book> hasPublishYearBetween(Integer publishYearStart, Integer publishYearEnd) {
        return (root, query, cb) -> {
            if (publishYearStart != null && publishYearEnd != null) {
                return cb.between(root.get("publishYear"), publishYearStart, publishYearEnd);
            } else if (publishYearStart != null) {
                return cb.greaterThanOrEqualTo(root.get("publishYear"), publishYearStart);
            } else if (publishYearEnd != null) {
                return cb.lessThanOrEqualTo(root.get("publishYear"), publishYearEnd);
            }
            return cb.conjunction();
        };
    }

    public Specification<Book> isAvailable(Boolean available) {
        return (root, query, cb) -> {
            if (available != null) {
                return cb.equal(root.get("isAvailable"), available);
            }
            return cb.conjunction();
        };
    }

    public Specification<Book> isDeleted(Boolean deleted) {
        return (root, query, cb) -> {
            if (deleted != null) {
                return cb.equal(root.get("isDeleted"), deleted);
            }
            return cb.conjunction();
        };
    }
}
