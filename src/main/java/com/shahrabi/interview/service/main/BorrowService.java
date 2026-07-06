package com.shahrabi.interview.service.main;

import com.shahrabi.interview.common.http.PagedResponseDto;
import com.shahrabi.interview.service.main.dto.BorrowDto;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface BorrowService {
    PagedResponseDto<BorrowDto.ReportBorrowDto> findAll(BorrowDto.QueryBorrowDto query, Pageable pageable);

    BorrowDto.CommandBorrowBookDto findById(UUID id);

    BorrowDto.CommandBorrowBookDto borrowBook(BorrowDto.BorrowBookDto dto);

    Boolean returnBook(String isbn);
}
