package com.antdevrealm.reviewmicroservice.web.dto;

import java.util.UUID;

public record ReviewResponseDTO(UUID id,
                                UUID authorId,
                                UUID subjectId,
                                String body) {
}
