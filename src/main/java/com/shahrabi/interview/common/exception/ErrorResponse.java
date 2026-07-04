package com.shahrabi.interview.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorResponse {
    private final boolean success;
    private final String message;
    private final long timestamp;
}