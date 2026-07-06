package com.shahrabi.interview.web.rest.controller.main;

import com.shahrabi.interview.common.http.PagedResponseDto;
import com.shahrabi.interview.model.BorrowSortField;
import com.shahrabi.interview.service.main.BorrowService;
import com.shahrabi.interview.service.main.dto.BorrowDto;
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
@RequiredArgsConstructor
public class BorrowController {

    private final BorrowService service;

    @GetMapping("/search")
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
    public ResponseEntity<BorrowDto.CommandBorrowBookDto> findById(@PathVariable UUID id) {
        BorrowDto.CommandBorrowBookDto borrowBookDto = service.findById(id);
        return ResponseEntity.ok().body(borrowBookDto);
    }

    @PostMapping
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
    public ResponseEntity<Void> returnBook(@PathVariable String isbn) {
        service.returnBook(isbn);
        return ResponseEntity.ok().build();
    }
}
