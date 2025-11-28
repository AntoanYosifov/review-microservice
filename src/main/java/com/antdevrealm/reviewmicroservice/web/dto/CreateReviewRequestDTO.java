package com.antdevrealm.reviewmicroservice.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreateReviewRequestDTO(
        @NotNull(message = "Author ID is required")
        UUID authorId,

        @NotNull(message = "Subject ID is required")
        UUID subjectId,

        @NotBlank(message = "Review body is required")
        String body
) {}
