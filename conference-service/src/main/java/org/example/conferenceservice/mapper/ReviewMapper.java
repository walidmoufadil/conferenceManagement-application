package org.example.conferenceservice.mapper;

import org.example.conferenceservice.dto.ReviewRequestDTO;
import org.example.conferenceservice.dto.ReviewResponseDTO;
import org.example.conferenceservice.entity.Review;
import org.springframework.stereotype.Service;

@Service
public class ReviewMapper {
    public  ReviewResponseDTO toDto(Review review) {
        return ReviewResponseDTO.builder()
                .id(review.getId())
                .date(review.getDate())
                .commentaire(review.getCommentaire())
                .build();
    }
    public  Review toEntity(ReviewRequestDTO reviewDTO) {
        return Review.builder()
                .date(reviewDTO.getDate())
                .commentaire(reviewDTO.getCommentaire())
                .build();
    }
}
