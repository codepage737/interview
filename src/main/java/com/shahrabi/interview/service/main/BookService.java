package com.shahrabi.interview.service.main;

import com.shahrabi.interview.common.http.PagedResponseDto;
import com.shahrabi.interview.service.main.dto.BookDto;
import org.springframework.data.domain.Pageable;

public interface BookService {

    PagedResponseDto<BookDto.ReportingBookDto> findAll(BookDto.QueryBookDto query, Pageable pageable);

    BookDto.CommandBookDto findByIsbn(String isbn);

    BookDto.CommandBookDto saveBook(BookDto.CommandBookDto dto);

    BookDto.CommandBookDto update(BookDto.CommandBookDto dto);

    void deleteByIsbn(String isbn);

    BookDto.CommandBookDto markBookAsUnAvailableAndReturn(String isbn);

    BookDto.CommandBookDto markBookAsAvailableAndReturn(String isbn);
}
