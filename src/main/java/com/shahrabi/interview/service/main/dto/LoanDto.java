package com.shahrabi.interview.service.main.dto;

import com.shahrabi.interview.domain.main.Book;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

public class LoanDto {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode
    @ToString
    public static final class CommandLoanDto {
        private UUID id;
        private Book book;
        private UUID bookId;
        private String borrowerName;
        private LocalDateTime loanDate;
        private LocalDateTime returnDate;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode
    @ToString
    public static final class QueryLoanDto {
        private UUID id;
        private Book book;
        private UUID bookId;
        private String borrowerName;
        private LocalDateTime loanDate;
        private LocalDateTime returnDate;
    }
}
