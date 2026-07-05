package com.shahrabi.interview.service.main.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

public class BookDto {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode
    @ToString
    public static final class CommandBookDto {
        @NotBlank(message = "{error.book.title.required}")
        private String title;
        @NotBlank(message = "{error.book.author_name.required}")
        private String authorName;
        @NotBlank(message = "{error.book.isbn.required}")
        private String isbn;
        @NotNull(message = "{error.book.publish_year.required}")
        private Integer publishYear;
        @JsonProperty(access = JsonProperty.Access.READ_ONLY)
        @Schema(accessMode = Schema.AccessMode.READ_ONLY)
        private Boolean isAvailable;
        @JsonProperty(access = JsonProperty.Access.READ_ONLY)
        @Schema(accessMode = Schema.AccessMode.READ_ONLY)
        private Boolean isDeleted;
        @JsonProperty(access = JsonProperty.Access.READ_ONLY)
        @Schema(accessMode = Schema.AccessMode.READ_ONLY)
        private UUID id;
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
