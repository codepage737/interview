package com.shahrabi.interview.service.main.impl;

import com.shahrabi.interview.common.http.PagedResponseDto;
import com.shahrabi.interview.domain.main.Book;
import com.shahrabi.interview.repository.main.BookRepository;
import com.shahrabi.interview.service.main.BookService;
import com.shahrabi.interview.service.main.dto.BookDto;
import com.shahrabi.interview.service.main.exception.BookAlreadyBorrowedException;
import com.shahrabi.interview.service.main.exception.BookAlreadyDeletedException;
import com.shahrabi.interview.service.main.exception.BookNotBorrowedException;
import com.shahrabi.interview.service.main.exception.DuplicateIsbnException;
import com.shahrabi.interview.service.main.mapper.impl.BookMapper;
import com.shahrabi.interview.service.main.specification.BookSpecification;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookMapper mapper;
    private final BookRepository repository;
    private final BookSpecification specification;

    @Override
    @Transactional(readOnly = true)
    public PagedResponseDto<BookDto.ReportingBookDto> findAll(BookDto.QueryBookDto query, Pageable pageable) {
        Specification<Book> allSpecifications = findAllAvailableBooks(query);
        Page<Book> all = repository.findAll(allSpecifications, pageable);
        Page<BookDto.ReportingBookDto> map = all.map(mapper::toReportDto);
        return new PagedResponseDto<>(map);
    }

    private Specification<Book> findAllAvailableBooks(BookDto.QueryBookDto query) {
        Specification<Book> allSpecifications = specification.getAllSpecifications(query);
        Specification<Book> notDeletedLogic = specification.isDeleted(false);
        return allSpecifications.and(notDeletedLogic);
    }

    @Override
    @Transactional
    public BookDto.CommandBookDto saveBook(BookDto.CommandBookDto dto) {
        if (repository.existsByIsbn(dto.getIsbn())) {
            throw new DuplicateIsbnException("error.book.isbn.duplicate");
        } else {
            Book entity = mapper.toEntity(dto);
            Book save = repository.save(entity);
            return mapper.toDto(save);
        }
    }

    @Override
    @Transactional
    public BookDto.CommandBookDto update(BookDto.CommandBookDto dto) {
        Book book = repository.findByIsbn(dto.getIsbn()).orElseThrow(() -> new EntityNotFoundException("error.book.isbn.not_found"));
        if (book.getIsDeleted()) {
            throw new BookAlreadyDeletedException("error.book.operation.already_deleted");
        }
        mapper.update(book, dto);
        Book save = repository.save(book);
        return mapper.toDto(save);
    }

    @Override
    @Transactional
    public void deleteByIsbn(String isbn) {
        Book book = repository.findByIsbn(isbn).orElseThrow(() -> new EntityNotFoundException("error.book.isbn.not_found"));
        if (book.getIsDeleted()) {
            throw new BookAlreadyDeletedException("error.book.operation.already_deleted");
        } else if (book.getIsAvailable()) {
            book.setIsDeleted(Boolean.TRUE);

        } else {
            throw new BookAlreadyBorrowedException("error.book.operation.active_borrow");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public BookDto.CommandBookDto findByIsbn(String isbn) {
        Book book = repository.findByIsbn(isbn).orElseThrow(() -> new EntityNotFoundException("error.book.isbn.not_found"));
        if (book.getIsDeleted()) {
            throw new BookAlreadyDeletedException("error.book.operation.already_deleted");
        }
        return mapper.toDto(book);
    }

    @Override
    public BookDto.CommandBookDto markBookAsUnAvailableAndReturn(String isbn) {
        Book book = repository.findByIsbn(isbn).orElseThrow(() -> new EntityNotFoundException("error.book.isbn.not_found"));
        if (!book.getIsAvailable()) {
            throw new BookAlreadyBorrowedException("error.book.operation.active_borrow");
        }
        if (book.getIsDeleted()) {
            throw new BookAlreadyDeletedException("error.book.operation.already_deleted");
        }
        book.setIsAvailable(Boolean.FALSE);
        return mapper.toDto(book);
    }

    @Override
    public BookDto.CommandBookDto markBookAsAvailableAndReturn(String isbn) {
        Book book = repository.findByIsbn(isbn).orElseThrow(() -> new EntityNotFoundException("error.book.isbn.not_found"));
        if (book.getIsAvailable()) {
            throw new BookNotBorrowedException("error.book.operation.inactive_borrow");
        }
        book.setIsAvailable(Boolean.TRUE);
        return mapper.toDto(book);
    }
}
