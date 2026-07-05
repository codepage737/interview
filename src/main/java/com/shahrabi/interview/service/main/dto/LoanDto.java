package com.shahrabi.interview.service.main.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
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
        @NotBlank(message = "{error.loan.isbn.empty}")
        private String isbn;
        @NotBlank(message = "{error.loan.borrower_name.empty}")
        private String borrowerName;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode
    @ToString
    @Builder
    public static final class CommandLoanBookDto {
        private String isbn;
        private String title;
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
        private String title;
        private String isbn;
        private String borrowerName;
        private LocalDateTime loanDateStart;
        private LocalDateTime loanDateEnd;
        private LocalDateTime returnDateStart;
        private LocalDateTime returnDateEnd;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode
    @ToString
    public static final class ReportLoansDto {
        private String title;
        private String isbn;
        private String borrowerName;
        private LocalDateTime loanDate;
        private LocalDateTime returnDate;
    }
}
