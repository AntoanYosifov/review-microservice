package com.antdevrealm.reviewmicroservice.web;

import com.antdevrealm.reviewmicroservice.service.ReviewService;
import com.antdevrealm.reviewmicroservice.web.dto.CreateRequestDTO;
import com.antdevrealm.reviewmicroservice.web.dto.ResponseDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/reviews")
public class Controller {
    private final ReviewService reviewService;

    @Autowired
    public Controller(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO> getById(@PathVariable UUID id) {
        ResponseDTO responseDTO = this.reviewService.getById(id);

        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/subject/{id}")
    public ResponseEntity<List<ResponseDTO>> getAllBySubjectId(@PathVariable("id") UUID subjectId) {
         List<ResponseDTO> responseDTOS = this.reviewService.getAllBySubjectId(subjectId);

         return ResponseEntity.ok(responseDTOS);
    }

    @PostMapping
    public ResponseEntity<ResponseDTO> create(@RequestBody @Valid CreateRequestDTO dto) {
        ResponseDTO responseDTO = this.reviewService.create(dto);

        URI uriLocation = URI.create("/api/v1/reviews/" + responseDTO.id());
        return ResponseEntity.created(uriLocation).body(responseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        this.reviewService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
