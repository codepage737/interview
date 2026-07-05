package com.shahrabi.interview.web.rest.controller.main;

import com.shahrabi.interview.service.main.dto.LoanDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(value = "/api/v1/loan")
public class LoanController {

    @GetMapping
    public ResponseEntity<Page<LoanDto.BorrowBookDto>> findAll(
            @Min(value = 1) @RequestParam(defaultValue = "0") int currentPage,
            @Min(value = 10) @RequestParam(defaultValue = "10") int pageSize,
            @Valid @ModelAttribute LoanDto.QueryLoanDto query
    ) {
        return null;
    }

    @PostMapping(value = "/borrow")
    public ResponseEntity<LoanDto.BorrowBookDto> borrowBook(@Valid @RequestBody LoanDto.BorrowBookDto dto) {
        return null;
    }

    @PostMapping(value = "/return/{bookId}")
    public ResponseEntity<Void> returnBook(@PathVariable UUID bookId) {
        return ResponseEntity.ok().build();
    }
}
