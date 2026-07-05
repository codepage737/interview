package com.shahrabi.interview.service.main.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

public class LoanDto {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode
    @ToString
    public static final class BorrowBookDto {
        @NotBlank(message = "شناسه کتاب نمی‌تواند خالی باشد")
        private String isbn;
        @NotBlank(message = "اسم غرض گیرنده کتاب نمی‌تواند خالی باشد")
        private String borrowerName;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode
    @ToString
    @Builder
    public static final class CommandLoanBookDto {
        private UUID bookId;
        private String isbn;
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
        private String bookTitle;
        private String isbn;
        private String borrowerName;
        private LocalDateTime loanDateStart;
        private LocalDateTime loanDateEnd;
        private LocalDateTime returnDateStart;
        private LocalDateTime returnDateEnd;
    }

    public static final class ReportLoansDto {
        private UUID id;
        private UUID bookId;
        private String isbn;
        private String borrowerName;
        private LocalDateTime loanDate;
        private LocalDateTime returnDate;
    }
}
