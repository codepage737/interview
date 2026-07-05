package com.shahrabi.interview.service.main;

import com.shahrabi.interview.common.http.PagedResponseDto;
import com.shahrabi.interview.service.main.dto.BookDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

public interface BookService {

    PagedResponseDto<BookDto.ReportingBookDto> findAll(BookDto.QueryBookDto query, Pageable pageable);

    BookDto.CommandBookDto saveBook(BookDto.CommandBookDto dto);

    BookDto.CommandBookDto update(BookDto.CommandBookDto dto);

    Void deleteById(UUID bookId);
}
