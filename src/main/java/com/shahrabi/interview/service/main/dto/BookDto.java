package com.shahrabi.interview.service.main.dto;

import lombok.*;

import java.util.UUID;

public class BookDto {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode
    @ToString
    public static final class CommandBookDto {
        private String title;
        private String authorName;
        private String isbn;
        private Integer publishYear;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode
    @ToString
    public static final class QueryBookDto {
        private UUID id;
        private String title;
        private String authorName;
        private String isbn;
        private Integer publishYear;
        private Integer publishYearStart;
        private Integer publishYearEnd;
        private Boolean isAvailable;
        private Boolean isDeleted;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode
    @ToString
    public static final class ReportingBookDto {
        private UUID id;
        private String title;
        private String authorName;
        private String isbn;
        private Integer publishYear;
        private Boolean isAvailable;
        private Boolean isDeleted;
    }
}
