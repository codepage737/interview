package com.shahrabi.interview.service.main.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
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
        @JsonProperty(access = JsonProperty.Access.READ_ONLY)
        @Schema(accessMode = Schema.AccessMode.READ_ONLY)
        private Boolean isAvailable;
        @JsonProperty(access = JsonProperty.Access.READ_ONLY)
        @Schema(accessMode = Schema.AccessMode.READ_ONLY)
        private Boolean isDeleted;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode
    @ToString
    public static final class QueryBookDto {
        private UUID id;
        private String isbn;
        private String title;
        private String authorName;
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
