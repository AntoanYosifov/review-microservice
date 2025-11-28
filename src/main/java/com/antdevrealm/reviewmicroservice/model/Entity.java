package com.antdevrealm.reviewmicroservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

@jakarta.persistence.Entity
@Table(name = "reviews")
public class Entity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID authorId;

    @Column(nullable = false)
    private UUID subjectId;

    @Column(nullable = false)
    private String body;
}
