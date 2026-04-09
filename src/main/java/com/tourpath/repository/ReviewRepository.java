package com.tourpath.repository;

import com.tourpath.model.Review;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends MongoRepository<Review, String> {
    List<Review> findByPlaceIdAndActiveTrueOrderByCreatedAtDesc(String placeId);

    List<Review> findByUserId(String userId);

    Optional<Review> findByPlaceIdAndUserId(String placeId, String userId);

    long countByPlaceId(String placeId);

    @Aggregation(pipeline = {
            "{ $match: { placeId: ?0, active: true } }",
            "{ $group: { _id: null, avgRating: { $avg: '$rating' } } }"
    })
    Double calculateAverageRating(String placeId);
}
