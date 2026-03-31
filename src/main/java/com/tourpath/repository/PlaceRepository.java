package com.tourpath.repository;

import com.tourpath.model.Place;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface PlaceRepository extends MongoRepository<Place, String> {
    List<Place> findByCityIdAndActiveTrue(String cityId);
    List<Place> findByCityIdAndCategoryAndActiveTrue(String cityId, Place.Category category);
    List<Place> findByActiveTrueOrderByFeaturedDescAverageRatingDesc();
    List<Place> findByFeaturedTrueAndActiveTrue();
    List<Place> findByOwnerIdAndActiveTrue(String ownerId);
    Optional<Place> findByOwnerId(String ownerId);
    @Query("{ $or: [ {'name': {$regex: ?0, $options:'i'}}, {'description': {$regex: ?0, $options:'i'}}, {'cityName': {$regex: ?0, $options:'i'}} ], 'active': true }")
    List<Place> searchByText(String query);
    long countByCityIdAndActiveTrue(String cityId);
    long countByActiveTrue();
    long countByFeaturedTrue();
    long countByCategory(Place.Category category);
}
