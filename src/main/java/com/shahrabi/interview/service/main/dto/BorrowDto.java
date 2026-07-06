package com.shahrabi.interview.service.main.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

public class BorrowDto {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode
    @ToString
    @Builder
    public static final class BorrowBookDto {
        @NotBlank(message = "{error.borrow.isbn.empty}")
        private String isbn;
        @NotBlank(message = "{error.borrow.borrower_name.empty}")
        private String borrowerName;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode
    @ToString
    @Builder
    public static final class CommandBorrowBookDto {
        private UUID bookId;
        private String borrowerName;
        private LocalDateTime borrowDate;
        private LocalDateTime returnDate;
        @JsonProperty(access = JsonProperty.Access.READ_ONLY)
        @Schema(accessMode = Schema.AccessMode.READ_ONLY)
        private UUID id;
        @JsonProperty(access = JsonProperty.Access.READ_ONLY)
        @Schema(accessMode = Schema.AccessMode.READ_ONLY)
        private String title;
        @JsonProperty(access = JsonProperty.Access.READ_ONLY)
        @Schema(accessMode = Schema.AccessMode.READ_ONLY)
        private String isbn;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode
    @ToString
    public static final class QueryBorrowDto {
        private String title;
        private String isbn;
        private String borrowerName;
        private LocalDateTime borrowDateStart;
        private LocalDateTime borrowDateEnd;
        private LocalDateTime returnDateStart;
        private LocalDateTime returnDateEnd;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode
    @ToString
    public static final class ReportBorrowDto {
        private String title;
        private String isbn;
        private String borrowerName;
        private LocalDateTime borrowDate;
        private LocalDateTime returnDate;
        private UUID id;
    }
}
