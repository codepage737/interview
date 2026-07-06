package com.shahrabi.interview.web.rest.controller.main;

import com.shahrabi.interview.common.exception.ErrorResponse;
import com.shahrabi.interview.common.http.PagedResponseDto;
import com.shahrabi.interview.model.BookSortField;
import com.shahrabi.interview.service.main.BookService;
import com.shahrabi.interview.service.main.dto.BookDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping(value = "/api/v1/book")
@Tag(name = "Book Management", description = "CRUD operations on Book entity")
@RequiredArgsConstructor
public class BookController {

    private final BookService service;

    @GetMapping(value = "/search")
    @Operation(summary = "Search books with pagination and sorting", description = "Retrieve a filtered, paginated and sorted list of books based on criteria.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved the paginated list")
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
    @Operation(summary = "Get a book by ISBN", description = "Provide an ISBN to get a specific book.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book found successfully"),
            @ApiResponse(responseCode = "404", description = "Book not found with the provided ISBN",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<BookDto.CommandBookDto> findByIsbn(@PathVariable String isbn) {
        BookDto.CommandBookDto bookDto = service.findByIsbn(isbn);
        return ResponseEntity.ok().body(bookDto);
    }

    @PostMapping
    @Operation(summary = "Create a new book", description = "Add a new book to library.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Book created successfully"),
            @ApiResponse(responseCode = "409", description = "Book with this ISBN already exists",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<BookDto.CommandBookDto> saveBook(@Valid @RequestBody BookDto.CommandBookDto bookDto) {
        BookDto.CommandBookDto savedDto = service.saveBook(bookDto);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{isbn}")
                .buildAndExpand(savedDto.getIsbn())
                .toUri();
        return ResponseEntity.created(location).body(savedDto);
    }

    @PutMapping
    @Operation(summary = "Update an existing book", description = "Updates book details. Throws an error if the book is deleted or missing.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book updated successfully"),
            @ApiResponse(responseCode = "404", description = "Book not found with the provided ISBN",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    public ResponseEntity<BookDto.CommandBookDto> update(
            @Valid @RequestBody BookDto.CommandBookDto bookDto
    ) {
        BookDto.CommandBookDto updatedDto = service.update(bookDto);
        return ResponseEntity.ok().body(updatedDto);
    }

    @DeleteMapping(value = "/{isbn}")
    @Operation(summary = "Delete a book by ISBN", description = "Logical soft-delete on the book records.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Book deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Book not found with the provided ISBN",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Book already borrowed",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<Void> deleteById(@PathVariable String isbn) {
        service.deleteByIsbn(isbn);
        return ResponseEntity.noContent().build();
    }
}
