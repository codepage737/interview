package com.shahrabi.interview.service.main.impl;

import com.shahrabi.interview.common.http.PagedResponseDto;
import com.shahrabi.interview.domain.main.Book;
import com.shahrabi.interview.domain.main.Loan;
import com.shahrabi.interview.repository.main.LoanRepository;
import com.shahrabi.interview.service.main.BookService;
import com.shahrabi.interview.service.main.LoanService;
import com.shahrabi.interview.service.main.dto.LoanDto;
import com.shahrabi.interview.service.main.exception.BookAlreadyBorrowedException;
import com.shahrabi.interview.service.main.exception.BookNotBorrowedException;
import com.shahrabi.interview.service.main.mapper.impl.LoanMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
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

    @Override
    public PagedResponseDto<LoanDto.ReportLoansDto> findAll(LoanDto.QueryLoanDto query, Pageable pageable) {
        return null;
    }

    @Override
    @Transactional
    public LoanDto.CommandLoanBookDto borrowBook(LoanDto.BorrowBookDto dto) {
        Book book = bookService.findByIsbn(dto.getIsbn());
        if (book.getIsAvailable()) {
            book.setIsAvailable(Boolean.FALSE);
            LoanDto.CommandLoanBookDto loanBookDto = LoanDto.CommandLoanBookDto.builder()
                    .bookId(book.getId())
                    .isbn(dto.getIsbn())
                    .borrowerName(dto.getBorrowerName())
                    .loanDate(LocalDateTime.now()).build();
            Loan entity = mapper.toEntity(loanBookDto);
            Loan save = repository.save(entity);
            return mapper.toDto(save);
        } else {
            throw new BookAlreadyBorrowedException("error.book.operation.active_loan");
        }
    }

    @Override
    @Transactional
    public void returnBook(String isbn) {
        Book book = bookService.findByIsbn(isbn);
        if (!book.getIsAvailable()) {
            Optional<Loan> loanOptional = repository.findFirstByBookIdAndReturnDateIsNullOrderByLoanDateDesc(book.getId());
            if (loanOptional.isPresent()) {
                Loan loan = loanOptional.get();
                loan.setReturnDate(LocalDateTime.now());
                book.setIsAvailable(Boolean.TRUE);
            } else {
                throw new RuntimeException();
            }
        } else {
            throw new BookNotBorrowedException("error.book.operation.inactive_loan");
        }
    }
}
