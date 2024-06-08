package com.harrishjoshi.springaop.audit.trails.book;

import jakarta.validation.constraints.NotBlank;

public record BookRequest(
        @NotBlank(message = "Title is required")
        String title,
        @NotBlank(message = "Description is required")
        String description,
        @NotBlank(message = "Edition is required")
        String edition
) {
}
