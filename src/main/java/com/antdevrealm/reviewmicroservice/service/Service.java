package com.antdevrealm.reviewmicroservice.service;

import com.antdevrealm.reviewmicroservice.exception.ResourceNotFoundException;
import com.antdevrealm.reviewmicroservice.model.Entity;
import com.antdevrealm.reviewmicroservice.repository.Repository;
import com.antdevrealm.reviewmicroservice.web.dto.CreateRequestDTO;
import com.antdevrealm.reviewmicroservice.web.dto.ResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

@org.springframework.stereotype.Service
public class Service {
    private final Repository repository;

    @Autowired
    public Service(Repository repository) {
        this.repository = repository;
    }

    public ResponseDTO getById(UUID id) {
        Entity entity = this.repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Review with ID: %s not found", id)));

        return mapToResponseDto(entity);
    }

    public ResponseDTO create(CreateRequestDTO dto) {
        Entity entity = this.mapToEntity(dto);
        return mapToResponseDto(this.repository.save(entity));
    }

    private Entity mapToEntity(CreateRequestDTO dto) {
       return Entity.builder()
               .authorId(dto.authorId())
               .subjectId(dto.subjectId())
               .body(dto.body())
               .build();
    }

    private ResponseDTO mapToResponseDto(Entity entity) {
        return new ResponseDTO(entity.getId(),
                entity.getAuthorId(),
                entity.getSubjectId(),
                entity.getBody());
    }
}
