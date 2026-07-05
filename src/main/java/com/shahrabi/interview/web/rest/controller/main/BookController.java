package com.shahrabi.interview.web.rest.controller.main;

import com.shahrabi.interview.service.main.BookService;
import com.shahrabi.interview.service.main.dto.BookDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(value = "/api/v1/book")
@RequiredArgsConstructor
public class BookController {

    private final BookService service;

    @GetMapping
    public ResponseEntity<Page<BookDto.CommandBookDto>> findAll(
            @Min(value = 1) @RequestParam(defaultValue = "0") int currentPage,
            @Min(value = 10) @RequestParam(defaultValue = "10") int pageSize,
            @Valid @ModelAttribute BookDto.QueryBookDto query
    ) {
        Page<BookDto.CommandBookDto> all = service.findAll(currentPage, pageSize, query);
        return ResponseEntity.ok().body(all);
    }

    @PostMapping
    public ResponseEntity<BookDto.CommandBookDto> saveBook(@Valid @RequestBody BookDto.CommandBookDto bookDto) {
        BookDto.CommandBookDto savedDto = service.saveBook(bookDto);
        return ResponseEntity.ok().body(savedDto);
    }

    @PutMapping
    public ResponseEntity<Page<BookDto.CommandBookDto>> update(
            @Valid @RequestBody BookDto.CommandBookDto bookDto
    ) {
        Page<BookDto.CommandBookDto> updatedDto = service.update(bookDto);
        return ResponseEntity.ok().body(updatedDto);
    }

    @DeleteMapping(value = "/{bookId}")
    public ResponseEntity<Void> deleteById(@PathVariable UUID bookId) {
        service.deleteById(bookId);
        return ResponseEntity.ok().build();
    }
}
