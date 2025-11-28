package com.antdevrealm.reviewmicroservice.web;

import com.antdevrealm.reviewmicroservice.service.ReviewService;
import com.antdevrealm.reviewmicroservice.web.dto.CreateRequestDTO;
import com.antdevrealm.reviewmicroservice.web.dto.ResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(Controller.class)
public class ControllerATest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ReviewService reviewService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void whenGetById_andReviewExists_thenReturnsOkWithResponseDTO() throws Exception {
        UUID reviewId = UUID.randomUUID();
        UUID authorId = UUID.randomUUID();
        UUID subjectId = UUID.randomUUID();
        String body = "Great product!";

        ResponseDTO responseDTO = new ResponseDTO(reviewId, authorId, subjectId, body);
        when(reviewService.getById(reviewId)).thenReturn(responseDTO);

        mockMvc.perform(get("/api/v1/reviews/{id}", reviewId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(reviewId.toString()))
                .andExpect(jsonPath("$.authorId").value(authorId.toString()))
                .andExpect(jsonPath("$.subjectId").value(subjectId.toString()))
                .andExpect(jsonPath("$.body").value(body));
    }

    @Test
    void whenGetAllBySubjectId_andReviewsExist_thenReturnsOkWithResponseDTOList() throws Exception {
        UUID subjectId = UUID.randomUUID();
        UUID authorId1 = UUID.randomUUID();
        UUID authorId2 = UUID.randomUUID();
        UUID reviewId1 = UUID.randomUUID();
        UUID reviewId2 = UUID.randomUUID();

        ResponseDTO responseDTO1 = new ResponseDTO(reviewId1, authorId1, subjectId, "First review");
        ResponseDTO responseDTO2 = new ResponseDTO(reviewId2, authorId2, subjectId, "Second review");
        List<ResponseDTO> responseDTOs = List.of(responseDTO1, responseDTO2);

        when(reviewService.getAllBySubjectId(subjectId)).thenReturn(responseDTOs);

        mockMvc.perform(get("/api/v1/reviews/subject/{id}", subjectId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(reviewId1.toString()))
                .andExpect(jsonPath("$[0].authorId").value(authorId1.toString()))
                .andExpect(jsonPath("$[0].body").value("First review"))
                .andExpect(jsonPath("$[1].id").value(reviewId2.toString()))
                .andExpect(jsonPath("$[1].authorId").value(authorId2.toString()))
                .andExpect(jsonPath("$[1].body").value("Second review"));
    }

    @Test
    void whenCreateReview_andValidDtoProvided_thenReturnsCreatedWithResponseDTOAndLocationHeader() throws Exception {
        UUID authorId = UUID.randomUUID();
        UUID subjectId = UUID.randomUUID();
        UUID reviewId = UUID.randomUUID();
        String body = "Excellent quality product!";

        CreateRequestDTO createRequestDTO = new CreateRequestDTO(authorId, subjectId, body);
        ResponseDTO responseDTO = new ResponseDTO(reviewId, authorId, subjectId, body);

        when(reviewService.create(any(CreateRequestDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/api/v1/reviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequestDTO)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(header().string("Location", "/api/v1/reviews/" + reviewId))
                .andExpect(jsonPath("$.id").value(reviewId.toString()))
                .andExpect(jsonPath("$.authorId").value(authorId.toString()))
                .andExpect(jsonPath("$.subjectId").value(subjectId.toString()))
                .andExpect(jsonPath("$.body").value(body));
    }

    @Test
    void whenDeleteReview_andReviewExists_thenReturnsNoContent() throws Exception {
        UUID reviewId = UUID.randomUUID();

        mockMvc.perform(delete("/api/v1/reviews/{id}", reviewId))
                .andExpect(status().isNoContent());

        verify(reviewService).delete(reviewId);
    }
}

