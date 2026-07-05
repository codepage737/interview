package com.shahrabi.interview.service.main.impl;

import com.shahrabi.interview.common.http.PagedResponseDto;
import com.shahrabi.interview.domain.main.Book;
import com.shahrabi.interview.repository.main.BookRepository;
import com.shahrabi.interview.service.main.BookService;
import com.shahrabi.interview.service.main.dto.BookDto;
import com.shahrabi.interview.service.main.mapper.impl.BookMapper;
import com.shahrabi.interview.service.main.specification.BookSpecification;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookMapper mapper;
    private final BookRepository repository;
    private final BookSpecification specification;

    @Override
    @Transactional(readOnly = true)
    public PagedResponseDto<BookDto.ReportingBookDto> findAll(BookDto.QueryBookDto query, Pageable pageable) {
        Specification<Book> allSpecifications = specification.getAllSpecifications(query);
        Page<Book> all = repository.findAll(allSpecifications, pageable);
        Page<BookDto.ReportingBookDto> map = all.map(mapper::toReportDto);
        return new PagedResponseDto(map);
    }

    @Override
    @Transactional
    public BookDto.CommandBookDto saveBook(BookDto.CommandBookDto dto) {
        Book entity = mapper.toEntity(dto);
        Book save = repository.save(entity);
        return mapper.toDto(save);
    }

    @Override
    @Transactional
    public BookDto.CommandBookDto update(BookDto.CommandBookDto dto) {
        Book book = repository.findByIsbn(dto.getIsbn()).orElseThrow(() -> new EntityNotFoundException(" id= " + dto.getIsbn()));
        mapper.update(book, dto);
        Book save = repository.save(book);
        return mapper.toDto(save);
    }

    @Override
    @Transactional
    public Void deleteById(UUID bookId) {
        return null;
    }
}
