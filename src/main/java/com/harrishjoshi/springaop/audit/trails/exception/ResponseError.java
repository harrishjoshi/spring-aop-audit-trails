package com.harrishjoshi.springaop.audit.trails.exception;

import lombok.Builder;

@Builder
public record ResponseError(
        String field,
        String error,
        String message
) {
}
