package com.antdevrealm.reviewmicroservice.service;

import com.antdevrealm.reviewmicroservice.exception.ResourceNotFoundException;
import com.antdevrealm.reviewmicroservice.model.ReviewEntity;
import com.antdevrealm.reviewmicroservice.repository.ReviewRepository;
import com.antdevrealm.reviewmicroservice.web.dto.CreateRequestDTO;
import com.antdevrealm.reviewmicroservice.web.dto.ResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;

    @Autowired
    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    public ResponseDTO getById(UUID id) {
        ReviewEntity reviewEntity = this.reviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Review with ID: %s not found", id)));

        return mapToResponseDto(reviewEntity);
    }

    public List<ResponseDTO> getAllBySubjectId(UUID subjectId) {
        List<ReviewEntity> entities = this.reviewRepository.findAllBySubjectId(subjectId);
        if (entities.isEmpty()) {
            return new ArrayList<>();
        }

        return entities.stream().map(this::mapToResponseDto).toList();
    }

    public ResponseDTO create(CreateRequestDTO dto) {
        ReviewEntity reviewEntity = this.mapToEntity(dto);
        return mapToResponseDto(this.reviewRepository.save(reviewEntity));
    }

    private ReviewEntity mapToEntity(CreateRequestDTO dto) {
        return ReviewEntity.builder()
                .authorId(dto.authorId())
                .subjectId(dto.subjectId())
                .body(dto.body())
                .build();
    }

    private ResponseDTO mapToResponseDto(ReviewEntity reviewEntity) {
        return new ResponseDTO(reviewEntity.getId(),
                reviewEntity.getAuthorId(),
                reviewEntity.getSubjectId(),
                reviewEntity.getBody());
    }


    public void delete(UUID id) {
        ReviewEntity reviewEntity = this.reviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Review with ID: %s not found", id)));

        this.reviewRepository.delete(reviewEntity);
    }
}
