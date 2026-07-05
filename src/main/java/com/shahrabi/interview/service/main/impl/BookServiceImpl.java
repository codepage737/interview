package com.shahrabi.interview.service.main.impl;

import com.shahrabi.interview.repository.main.BookRepository;
import com.shahrabi.interview.service.main.BookService;
import com.shahrabi.interview.service.main.dto.BookDto;
import com.shahrabi.interview.service.main.mapper.impl.BookMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookMapper mapper;
    private final BookRepository repository;

    @Override
    public Page<BookDto.CommandBookDto> findAll(int currentPage, int pageSize, BookDto.QueryBookDto query) {
        return null;
    }

    @Override
    public BookDto.CommandBookDto saveBook(BookDto.CommandBookDto dto) {
        return null;
    }

    @Override
    public Page<BookDto.CommandBookDto> update(BookDto.CommandBookDto dto) {
        return null;
    }

    @Override
    public Void deleteById(UUID bookId) {
        return null;
    }
}
