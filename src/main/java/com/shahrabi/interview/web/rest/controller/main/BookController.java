package com.shahrabi.interview.web.rest.controller.main;

import com.shahrabi.interview.common.http.PagedResponseDto;
import com.shahrabi.interview.model.BookSortField;
import com.shahrabi.interview.service.main.BookService;
import com.shahrabi.interview.service.main.dto.BookDto;
import com.shahrabi.interview.service.main.dto.LoanDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/book")
@RequiredArgsConstructor
public class BookController {

    private final BookService service;

    @GetMapping(value = "/search")
    public ResponseEntity<PagedResponseDto<BookDto.ReportingBookDto>> findAll(
            @Min(value = 0) @RequestParam(defaultValue = "0") int currentPage,
            @Min(value = 10) @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = BookSortField.DEFAULT_SORT) BookSortField sortBy,
            @RequestParam(defaultValue = "DESC") Sort.Direction sortDirection,
            @ModelAttribute BookDto.QueryBookDto query
    ) {
        Sort.Direction direction = Sort.Direction.fromString(sortDirection.name());
        Sort sort = Sort.by(direction, sortBy.name());
        Pageable pageable = PageRequest.of(currentPage, pageSize, sort);
        PagedResponseDto<BookDto.ReportingBookDto> all = service.findAll(query, pageable);
        return ResponseEntity.ok().body(all);
    }

    @GetMapping(value = "/{isbn}")
    public ResponseEntity<BookDto.CommandBookDto> findByIsbn(String isbn) {
        BookDto.CommandBookDto bookDto = service.findByIsbn(isbn);
        return ResponseEntity.ok().body(bookDto);
    }

    @PostMapping
    public ResponseEntity<BookDto.CommandBookDto> saveBook(@Valid @RequestBody BookDto.CommandBookDto bookDto) {
        BookDto.CommandBookDto savedDto = service.saveBook(bookDto);
        return ResponseEntity.ok().body(savedDto);
    }

    @PutMapping
    public ResponseEntity<BookDto.CommandBookDto> update(
            @Valid @RequestBody BookDto.CommandBookDto bookDto
    ) {
        BookDto.CommandBookDto updatedDto = service.update(bookDto);
        return ResponseEntity.ok().body(updatedDto);
    }

    @DeleteMapping(value = "/{isbnId}")
    public ResponseEntity<Void> deleteById(@PathVariable String isbnId) {
        service.deleteById(isbnId);
        return ResponseEntity.ok().build();
    }
}
