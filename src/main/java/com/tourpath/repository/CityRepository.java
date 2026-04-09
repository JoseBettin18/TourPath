package com.tourpath.repository;

import com.tourpath.model.City;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface CityRepository extends MongoRepository<City, String> {
    List<City> findByActiveTrueOrderByFeaturedDescNameAsc();

    List<City> findByFeaturedTrueAndActiveTrue();

    Optional<City> findByNameIgnoreCase(String name);

    @Query("{ 'name': { $regex: ?0, $options: 'i' }, 'active': true }")
    List<City> searchByName(String query);

    List<City> findByCountryIgnoreCaseAndActiveTrue(String country);

    List<City> findByContinentAndActiveTrue(String continent);
}
