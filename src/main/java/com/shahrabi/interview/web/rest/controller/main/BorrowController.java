package com.shahrabi.interview.web.rest.controller.main;

import com.shahrabi.interview.common.exception.ErrorResponse;
import com.shahrabi.interview.common.http.PagedResponseDto;
import com.shahrabi.interview.model.BorrowSortField;
import com.shahrabi.interview.service.main.BorrowService;
import com.shahrabi.interview.service.main.dto.BorrowDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping(value = "/api/v1/borrow")
@Tag(name = "Borrow Management", description = "APIs for borrowing/returning books, and tracking history")
@RequiredArgsConstructor
public class BorrowController {

    private final BorrowService service;

    @GetMapping("/search")
    @Operation(summary = "Search borrow records with pagination and sorting", description = "Retrieve a filtered, paginated and sorted list of book borrow transactions.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved the paginated list")
    public ResponseEntity<PagedResponseDto<BorrowDto.ReportBorrowDto>> findAll(
            @Min(value = 0) @RequestParam(defaultValue = "0") int currentPage,
            @Min(value = 10) @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = BorrowSortField.DEFAULT_SORT) BorrowSortField sortBy,
            @RequestParam(defaultValue = "DESC") Sort.Direction sortDirection,
            @ModelAttribute BorrowDto.QueryBorrowDto query
    ) {
        Sort.Direction direction = Sort.Direction.fromString(sortDirection.name());
        Sort sort = Sort.by(direction, sortBy.name());
        Pageable pageable = PageRequest.of(currentPage, pageSize, sort);
        PagedResponseDto<BorrowDto.ReportBorrowDto> all = service.findAll(query, pageable);
        return ResponseEntity.ok().body(all);
    }

    @GetMapping(value = "/{id}")
    @Operation(summary = "Get a borrow record by ID", description = "Provide a unique UUID to lookup a specific borrow transaction.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Record found successfully"),
            @ApiResponse(responseCode = "404", description = "Book not found with the provided ISBN",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<BorrowDto.CommandBorrowBookDto> findById(@PathVariable UUID id) {
        BorrowDto.CommandBorrowBookDto borrowBookDto = service.findById(id);
        return ResponseEntity.ok().body(borrowBookDto);
    }

    @PostMapping
    @Operation(summary = "Borrow a book", description = "Creates a new borrow record for a book.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Record found successfully"),
            @ApiResponse(responseCode = "404", description = "Book not found with the provided ISBN",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Book already borrowed",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<BorrowDto.CommandBorrowBookDto> borrowBook(@Valid @RequestBody BorrowDto.BorrowBookDto dto) {
        BorrowDto.CommandBorrowBookDto borrowBookDto = service.borrowBook(dto);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(borrowBookDto.getId())
                .toUri();
        return ResponseEntity.created(location).body(borrowBookDto);
    }

    @PostMapping(value = "/return/{isbn}")
    @Operation(summary = "Return a borrowed book", description = "Processes the return of a book by its ISBN, updating its availability status.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Record found successfully"),
            @ApiResponse(responseCode = "404", description = "Book not found with the provided ISBN",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Book not borrowed",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<Void> returnBook(@PathVariable String isbn) {
        service.returnBook(isbn);
        return ResponseEntity.ok().build();
    }
}
