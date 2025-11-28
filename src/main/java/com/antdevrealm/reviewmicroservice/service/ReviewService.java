package com.antdevrealm.reviewmicroservice.service;

import com.antdevrealm.reviewmicroservice.model.ReviewEntity;
import com.antdevrealm.reviewmicroservice.repository.ReviewRepository;
import com.antdevrealm.reviewmicroservice.web.dto.CreateReviewRequestDTO;
import com.antdevrealm.reviewmicroservice.web.dto.ReviewResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;

    @Autowired
    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    public ReviewResponseDTO create(CreateReviewRequestDTO dto) {
        ReviewEntity reviewEntity = this.mapToEntity(dto);
        return mapToResponseDto(this.reviewRepository.save(reviewEntity));
    }

    private ReviewEntity mapToEntity(CreateReviewRequestDTO dto) {
       return ReviewEntity.builder()
               .authorId(dto.authorId())
               .subjectId(dto.subjectId())
               .body(dto.body())
               .build();
    }

    private ReviewResponseDTO mapToResponseDto(ReviewEntity reviewEntity) {
        return new ReviewResponseDTO(reviewEntity.getId(),
                reviewEntity.getAuthorId(),
                reviewEntity.getSubjectId(),
                reviewEntity.getBody());
    }
}
