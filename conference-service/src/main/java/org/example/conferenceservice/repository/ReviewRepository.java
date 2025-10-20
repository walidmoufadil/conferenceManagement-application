package org.example.conferenceservice.repository;

import org.example.conferenceservice.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
}
