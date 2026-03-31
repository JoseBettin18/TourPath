package com.tourpath.service;

import com.tourpath.model.Review;
import com.tourpath.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ReviewService {

    @Autowired private ReviewRepository reviewRepository;
    @Autowired private PlaceService placeService;

    public Review addReview(Review review) {
        Optional<Review> existing = reviewRepository.findByPlaceIdAndUserId(
                review.getPlaceId(), review.getUserId());
        if (existing.isPresent()) {
            Review ex = existing.get();
            ex.setComment(review.getComment());
            ex.setRating(review.getRating());
            ex.setCreatedAt(LocalDateTime.now());
            Review saved = reviewRepository.save(ex);
            placeService.updateAverageRating(review.getPlaceId());
            return saved;
        }
        review.setCreatedAt(LocalDateTime.now());
        review.setActive(true);
        Review saved = reviewRepository.save(review);
        placeService.updateAverageRating(review.getPlaceId());
        return saved;
    }

    public List<Review> getReviewsByPlace(String placeId) {
        return reviewRepository.findByPlaceIdAndActiveTrueOrderByCreatedAtDesc(placeId);
    }

    public List<Review> getReviewsByUser(String userId) {
        return reviewRepository.findByUserId(userId);
    }

    public void deleteReview(String id) {
        Review r = reviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reseña no encontrada"));
        String placeId = r.getPlaceId();
        reviewRepository.deleteById(id);
        placeService.updateAverageRating(placeId);
    }
}
