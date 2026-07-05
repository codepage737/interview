package com.shahrabi.interview.service.main;

import com.shahrabi.interview.service.main.dto.BookDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

public interface BookService {

    Page<BookDto.CommandBookDto> findAll(
            @Min(value = 1) @RequestParam(defaultValue = "0") int currentPage,
            @Min(value = 10) @RequestParam(defaultValue = "10") int pageSize,
            @Valid @ModelAttribute BookDto.QueryBookDto query
    );

    BookDto.CommandBookDto saveBook(@Valid @RequestBody BookDto.CommandBookDto dto);

    Page<BookDto.CommandBookDto> update(@Valid @RequestBody BookDto.CommandBookDto dto);

    Void deleteById(@PathVariable UUID bookId);
}
