package com.antdevrealm.reviewmicroservice.repository;

import com.antdevrealm.reviewmicroservice.model.Entity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

@org.springframework.stereotype.Repository
public interface Repository extends JpaRepository<Entity, UUID> {
}
