package com.shahrabi.interview.service.main;

import com.shahrabi.interview.common.http.PagedResponseDto;
import com.shahrabi.interview.service.main.dto.LoanDto;
import org.springframework.data.domain.Pageable;

public interface LoanService {
    PagedResponseDto<LoanDto.ReportLoansDto> findAll(LoanDto.QueryLoanDto query, Pageable pageable);

    LoanDto.CommandLoanBookDto borrowBook(LoanDto.BorrowBookDto dto);

    void returnBook(String isbn);
}
