package com.shahrabi.interview.service.main.impl;

import com.shahrabi.interview.common.http.PagedResponseDto;
import com.shahrabi.interview.domain.main.Loan;
import com.shahrabi.interview.repository.main.LoanRepository;
import com.shahrabi.interview.service.main.BookService;
import com.shahrabi.interview.service.main.LoanService;
import com.shahrabi.interview.service.main.dto.BookDto;
import com.shahrabi.interview.service.main.dto.LoanDto;
import com.shahrabi.interview.service.main.mapper.impl.LoanMapper;
import com.shahrabi.interview.service.main.specification.LoanSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LoanServiceImpl implements LoanService {

    private final LoanMapper mapper;
    private final LoanRepository repository;
    private final BookService bookService;
    private final LoanSpecification specification;

    @Override
    public PagedResponseDto<LoanDto.ReportLoansDto> findAll(LoanDto.QueryLoanDto query, Pageable pageable) {
        Specification<Loan> allSpecifications = specification.getAllSpecifications(query);
        Page<Loan> all = repository.findAll(allSpecifications, pageable);
        Page<LoanDto.ReportLoansDto> map = all.map(mapper::toReportDto);
        return new PagedResponseDto<>(map);
    }

    @Override
    @Transactional
    public LoanDto.CommandLoanBookDto borrowBook(LoanDto.BorrowBookDto dto) {
        BookDto.CommandBookDto book = bookService.markBookAsUnAvailableAndReturn(dto.getIsbn());
        LoanDto.CommandLoanBookDto loanBookDto = LoanDto.CommandLoanBookDto.builder()
                .bookId(book.getId())
                .isbn(dto.getIsbn())
                .title(book.getTitle())
                .borrowerName(dto.getBorrowerName())
                .loanDate(LocalDateTime.now()).build();
        Loan entity = mapper.toEntity(loanBookDto);
        Loan save = repository.save(entity);
        LoanDto.CommandLoanBookDto reportDto = mapper.toDto(save);
        return loanBookDto;
    }

    @Override
    @Transactional
    public Boolean returnBook(String isbn) {
        BookDto.CommandBookDto bookDto = bookService.markBookAsAvailableAndReturn(isbn);
        Optional<Loan> loanOptional = repository.findFirstByBookIdAndReturnDateIsNullOrderByLoanDateDesc(bookDto.getId());
        if (loanOptional.isPresent()) {
            Loan loan = loanOptional.get();
            loan.setReturnDate(LocalDateTime.now());
            bookDto.setIsAvailable(Boolean.TRUE);
            return Boolean.TRUE;
        } else {
            throw new RuntimeException();
        }
    }
}
