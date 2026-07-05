package com.shahrabi.interview.common.http;

import org.springframework.data.domain.Page;

import java.util.List;

public record PagedResponseDto<T> (
    List<T> content,
    int pageNumber,
    int pageSize,
    long totalElements,
    int totalPages,
    boolean isLas
) {
    public PagedResponseDto(Page<T> page) {
        this(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isLast()
        );
    }
}
