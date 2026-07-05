package com.shahrabi.interview.service.main.impl;

import com.shahrabi.interview.repository.main.LoanRepository;
import com.shahrabi.interview.service.main.LoanService;
import com.shahrabi.interview.service.main.dto.LoanDto;
import com.shahrabi.interview.service.main.mapper.impl.LoanMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LoanServiceImpl implements LoanService {

    private final LoanMapper mapper;
    private final LoanRepository repository;

    @Override
    public Page<LoanDto.BorrowBookDto> findAll(int currentPage, int pageSize, LoanDto.QueryLoanDto query) {
        return null;
    }

    @Override
    public LoanDto.BorrowBookDto borrowBook(LoanDto.BorrowBookDto dto) {
        return null;
    }

    @Override
    public void returnBook(UUID bookId) {

    }
}
