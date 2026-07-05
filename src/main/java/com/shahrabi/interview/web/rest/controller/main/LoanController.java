package com.shahrabi.interview.web.rest.controller.main;

import com.shahrabi.interview.common.http.PagedResponseDto;
import com.shahrabi.interview.model.LoanSortField;
import com.shahrabi.interview.service.main.LoanService;
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
@RequestMapping(value = "/api/v1/loan")
@RequiredArgsConstructor
public class LoanController {

    private final LoanService service;

    @GetMapping
    public ResponseEntity<PagedResponseDto<LoanDto.ReportLoansDto>> findAll(
            @Min(value = 0) @RequestParam(defaultValue = "0") int currentPage,
            @Min(value = 10) @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = LoanSortField.DEFAULT_SORT) LoanSortField sortBy,
            @RequestParam(defaultValue = "DESC") Sort.Direction sortDirection,
            @ModelAttribute LoanDto.QueryLoanDto query
    ) {
        Sort.Direction direction = Sort.Direction.fromString(sortDirection.name());
        Sort sort = Sort.by(direction, sortBy.name());
        Pageable pageable = PageRequest.of(currentPage, pageSize, sort);
        PagedResponseDto<LoanDto.ReportLoansDto> all = service.findAll(query, pageable);
        return ResponseEntity.ok().body(all);
    }

    @PostMapping(value = "/borrow")
    public ResponseEntity<LoanDto.CommandLoanBookDto> borrowBook(@Valid @RequestBody LoanDto.BorrowBookDto dto) {
        LoanDto.CommandLoanBookDto loanBookDto = service.borrowBook(dto);
        return ResponseEntity.ok().body(loanBookDto);
    }

    @PostMapping(value = "/return/{isbn}")
    public ResponseEntity<Void> returnBook(@PathVariable String isbn) {
        service.returnBook(isbn);
        return ResponseEntity.ok().build();
    }
}
