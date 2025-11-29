package com.antdevrealm.reviewmicroservice.service;

import com.antdevrealm.reviewmicroservice.exception.ResourceNotFoundException;
import com.antdevrealm.reviewmicroservice.web.dto.CreateRequestDTO;
import com.antdevrealm.reviewmicroservice.web.dto.ResponseDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class ReviewServiceITest {

    @Autowired
    private ReviewService reviewService;

    @Test
    void whenCreateReview_andDtoProvided_thenReviewIsSavedAndCanBeRetrievedById() {
        UUID authorId = UUID.randomUUID();
        UUID subjectId = UUID.randomUUID();
        String authorName = "John Doe";
        String body = "Great product, highly recommend!";

        CreateRequestDTO dto = new CreateRequestDTO(authorId, authorName, subjectId, body);

        ResponseDTO created = reviewService.create(dto);

        assertNotNull(created.id());
        assertEquals(authorId, created.authorId());
        assertEquals(authorName, created.authorName());
        assertEquals(subjectId, created.subjectId());
        assertEquals(body, created.body());

        ResponseDTO retrieved = reviewService.getById(created.id());
        assertEquals(created.id(), retrieved.id());
        assertEquals(authorId, retrieved.authorId());
        assertEquals(authorName, retrieved.authorName());
        assertEquals(subjectId, retrieved.subjectId());
        assertEquals(body, retrieved.body());
    }

    @Test
    void whenGetAllBySubjectId_andMultipleReviewsExist_thenReturnsAllReviewsForSubject() {
        UUID subjectId = UUID.randomUUID();
        UUID authorId1 = UUID.randomUUID();
        UUID authorId2 = UUID.randomUUID();

        CreateRequestDTO dto1 = new CreateRequestDTO(authorId1, "John Doe", subjectId, "First review");
        CreateRequestDTO dto2 = new CreateRequestDTO(authorId2, "Jane Smith", subjectId, "Second review");

        ResponseDTO review1 = reviewService.create(dto1);
        ResponseDTO review2 = reviewService.create(dto2);

        List<ResponseDTO> result = reviewService.getAllBySubjectId(subjectId);

        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(r -> r.id().equals(review1.id())));
        assertTrue(result.stream().anyMatch(r -> r.id().equals(review2.id())));
    }

    @Test
    void whenGetAllBySubjectId_andNoReviewsExist_thenReturnsEmptyList() {
        UUID subjectId = UUID.randomUUID();

        List<ResponseDTO> result = reviewService.getAllBySubjectId(subjectId);

        assertTrue(result.isEmpty());
    }

    @Test
    void whenDeleteReview_andReviewExists_thenReviewIsDeleted() {
        UUID authorId = UUID.randomUUID();
        UUID subjectId = UUID.randomUUID();
        CreateRequestDTO dto = new CreateRequestDTO(authorId, "John Doe", subjectId, "Review to delete");

        ResponseDTO created = reviewService.create(dto);
        UUID reviewId = created.id();

        reviewService.delete(reviewId);

        assertThrows(ResourceNotFoundException.class, () -> reviewService.getById(reviewId));
    }

    @Test
    void whenGetById_andReviewDoesNotExist_thenThrowsException() {
        UUID nonExistentId = UUID.randomUUID();

        assertThrows(ResourceNotFoundException.class, () -> reviewService.getById(nonExistentId));
    }
}

