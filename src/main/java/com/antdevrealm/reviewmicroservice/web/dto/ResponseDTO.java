package com.antdevrealm.reviewmicroservice.web.dto;

import java.util.UUID;

public record ResponseDTO(UUID id,
                          UUID authorId,
                          String authorName,
                          UUID subjectId,
                          String body) {
}
