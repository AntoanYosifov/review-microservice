package com.antdevrealm.reviewmicroservice.web;

import com.antdevrealm.reviewmicroservice.service.Service;
import com.antdevrealm.reviewmicroservice.web.dto.CreateRequestDTO;
import com.antdevrealm.reviewmicroservice.web.dto.ResponseDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/reviews")
public class Controller {
    private final Service reviewService;

    @Autowired
    public Controller(Service reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO> getById(@PathVariable UUID id) {
        ResponseDTO responseDTO = this.reviewService.getById(id);

        return ResponseEntity.ok(responseDTO);
    }

    @PostMapping
    public ResponseEntity<ResponseDTO> create(@RequestBody @Valid CreateRequestDTO dto) {
        ResponseDTO responseDTO = this.reviewService.create(dto);

        URI uriLocation = URI.create("/api/v1/reviews/" + responseDTO.id());
        return ResponseEntity.created(uriLocation).body(responseDTO);
    }
}
