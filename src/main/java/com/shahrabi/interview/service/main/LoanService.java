package com.shahrabi.interview.service.main;

import com.shahrabi.interview.service.main.dto.LoanDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

public interface LoanService {
    Page<LoanDto.BorrowBookDto> findAll(
            @Min(value = 1) @RequestParam(defaultValue = "0") int currentPage,
            @Min(value = 10) @RequestParam(defaultValue = "10") int pageSize,
            @Valid @ModelAttribute LoanDto.QueryLoanDto query
    );

    LoanDto.BorrowBookDto borrowBook(@Valid @RequestBody LoanDto.BorrowBookDto dto);

    void returnBook(@PathVariable UUID bookId);
}
