package com.antdevrealm.reviewmicroservice.web;

import com.antdevrealm.reviewmicroservice.service.ReviewService;
import com.antdevrealm.reviewmicroservice.web.dto.ResponseDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(Controller.class)
public class ControllerATest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ReviewService reviewService;

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
}

