package com.antdevrealm.reviewmicroservice.web;

import com.antdevrealm.reviewmicroservice.service.ReviewService;
import com.antdevrealm.reviewmicroservice.web.dto.CreateReviewRequestDTO;
import com.antdevrealm.reviewmicroservice.web.dto.ReviewResponseDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/reviews")
public class ReviewController {
    private final ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping
    public ResponseEntity<ReviewResponseDTO> create(@RequestBody @Valid CreateReviewRequestDTO dto) {
        ReviewResponseDTO responseDTO = this.reviewService.create(dto);

        URI uriLocation = URI.create("/api/v1/reviews/" + responseDTO.id());
        return ResponseEntity.created(uriLocation).body(responseDTO);
    }
}
