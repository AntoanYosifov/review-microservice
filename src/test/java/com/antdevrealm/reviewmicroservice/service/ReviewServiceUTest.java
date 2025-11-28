package com.antdevrealm.reviewmicroservice.service;

import com.antdevrealm.reviewmicroservice.exception.ResourceNotFoundException;
import com.antdevrealm.reviewmicroservice.model.ReviewEntity;
import com.antdevrealm.reviewmicroservice.repository.ReviewRepository;
import com.antdevrealm.reviewmicroservice.web.dto.CreateRequestDTO;
import com.antdevrealm.reviewmicroservice.web.dto.ResponseDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ReviewServiceUTest {

    @Mock
    private ReviewRepository reviewRepository;

    @InjectMocks
    private ReviewService reviewService;

    @Test
    void whenGetById_andRepositoryReturnsOptionalEmpty_thenThrowsException() {
        UUID reviewId = UUID.randomUUID();
        when(reviewRepository.findById(reviewId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> reviewService.getById(reviewId));
    }

    @Test
    void whenGetById_andRepositoryReturnsReviewEntity_thenMapsToResponseDTO() {
        UUID reviewId = UUID.randomUUID();
        UUID authorId = UUID.randomUUID();
        UUID subjectId = UUID.randomUUID();
        String body = "Great product!";

        ReviewEntity reviewEntity = ReviewEntity.builder()
                .id(reviewId)
                .authorId(authorId)
                .subjectId(subjectId)
                .body(body)
                .build();

        when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(reviewEntity));

        ResponseDTO result = reviewService.getById(reviewId);

        assertEquals(reviewId, result.id());
        assertEquals(authorId, result.authorId());
        assertEquals(subjectId, result.subjectId());
        assertEquals(body, result.body());
    }

    @Test
    void whenGetAllBySubjectId_andRepositoryReturnsEmptyList_thenReturnsEmptyList() {
        UUID subjectId = UUID.randomUUID();
        when(reviewRepository.findAllBySubjectId(subjectId)).thenReturn(new ArrayList<>());

        List<ResponseDTO> result = reviewService.getAllBySubjectId(subjectId);

        assertTrue(result.isEmpty());
    }

    @Test
    void whenGetAllBySubjectId_andRepositoryReturnsReviewEntities_thenMapsToResponseDTOList() {
        UUID subjectId = UUID.randomUUID();
        UUID authorId1 = UUID.randomUUID();
        UUID authorId2 = UUID.randomUUID();
        UUID reviewId1 = UUID.randomUUID();
        UUID reviewId2 = UUID.randomUUID();

        ReviewEntity reviewEntity1 = ReviewEntity.builder()
                .id(reviewId1)
                .authorId(authorId1)
                .subjectId(subjectId)
                .body("First review")
                .build();

        ReviewEntity reviewEntity2 = ReviewEntity.builder()
                .id(reviewId2)
                .authorId(authorId2)
                .subjectId(subjectId)
                .body("Second review")
                .build();

        List<ReviewEntity> entities = List.of(reviewEntity1, reviewEntity2);
        when(reviewRepository.findAllBySubjectId(subjectId)).thenReturn(entities);

        List<ResponseDTO> result = reviewService.getAllBySubjectId(subjectId);

        assertEquals(2, result.size());
        assertEquals(reviewId1, result.get(0).id());
        assertEquals(authorId1, result.get(0).authorId());
        assertEquals("First review", result.get(0).body());
        assertEquals(reviewId2, result.get(1).id());
        assertEquals(authorId2, result.get(1).authorId());
        assertEquals("Second review", result.get(1).body());
    }

    @Test
    void whenCreate_andDtoProvided_thenMapsToEntitySavesAndReturnsResponseDTO() {
        UUID authorId = UUID.randomUUID();
        UUID subjectId = UUID.randomUUID();
        String body = "Excellent quality!";

        CreateRequestDTO dto = new CreateRequestDTO(authorId, subjectId, body);

        ReviewEntity savedEntity = ReviewEntity.builder()
                .id(UUID.randomUUID())
                .authorId(authorId)
                .subjectId(subjectId)
                .body(body)
                .build();

        when(reviewRepository.save(any(ReviewEntity.class))).thenReturn(savedEntity);

        ResponseDTO result = reviewService.create(dto);

        assertNotNull(result.id());
        assertEquals(authorId, result.authorId());
        assertEquals(subjectId, result.subjectId());
        assertEquals(body, result.body());
        verify(reviewRepository).save(any(ReviewEntity.class));
    }

    @Test
    void whenDelete_andRepositoryReturnsOptionalEmpty_thenThrowsException() {
        UUID reviewId = UUID.randomUUID();
        when(reviewRepository.findById(reviewId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> reviewService.delete(reviewId));
    }

    @Test
    void whenDelete_andRepositoryReturnsReviewEntity_thenDeletesEntity() {
        UUID reviewId = UUID.randomUUID();
        ReviewEntity reviewEntity = ReviewEntity.builder()
                .id(reviewId)
                .authorId(UUID.randomUUID())
                .subjectId(UUID.randomUUID())
                .body("Review to delete")
                .build();

        when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(reviewEntity));

        reviewService.delete(reviewId);

        verify(reviewRepository).delete(reviewEntity);
    }
}

