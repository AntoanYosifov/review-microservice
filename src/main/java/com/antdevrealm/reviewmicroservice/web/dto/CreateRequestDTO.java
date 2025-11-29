package com.antdevrealm.reviewmicroservice.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreateRequestDTO(
        @NotNull(message = "Author ID is required")
        UUID authorId,

        @NotBlank(message = "Author name is required")
        String authorName,

        @NotNull(message = "Subject ID is required")
        UUID subjectId,

        @NotBlank(message = "Review body is required")
        String body
) {}
