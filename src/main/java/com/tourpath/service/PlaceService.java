package com.tourpath.service;

import com.tourpath.model.Place;
import com.tourpath.repository.PlaceRepository;
import com.tourpath.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PlaceService {

    @Autowired private PlaceRepository placeRepository;
    @Autowired private ReviewRepository reviewRepository;
    @Autowired private CityService cityService;

    public Place savePlace(Place place) {
        if (place.getId() == null) {
            place.setCreatedAt(LocalDateTime.now());
            place.setActive(true);
        }
        place.setUpdatedAt(LocalDateTime.now());
        Place saved = placeRepository.save(place);
        if (place.getCityId() != null) cityService.updateCityPlaceCount(place.getCityId());
        return saved;
    }

    public List<Place> getAllActivePlaces() {
        return placeRepository.findByActiveTrueOrderByFeaturedDescAverageRatingDesc();
    }

    public List<Place> getPlacesByCity(String cityId) {
        return placeRepository.findByCityIdAndActiveTrue(cityId);
    }

    public List<Place> getPlacesByCityAndCategory(String cityId, Place.Category category) {
        return placeRepository.findByCityIdAndCategoryAndActiveTrue(cityId, category);
    }

    public List<Place> getFeaturedPlaces() {
        return placeRepository.findByFeaturedTrueAndActiveTrue();
    }

    public Optional<Place> getPlaceById(String id) {
        return placeRepository.findById(id);
    }

    public Optional<Place> getPlaceByOwnerId(String ownerId) {
        return placeRepository.findByOwnerId(ownerId);
    }

    public List<Place> searchPlaces(String query) {
        return placeRepository.searchByText(query);
    }

    public Place updatePlace(String id, Place updated) {
        Place place = placeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Lugar no encontrado"));
        place.setName(updated.getName());
        place.setDescription(updated.getDescription());
        place.setCategory(updated.getCategory());
        place.setAddress(updated.getAddress());
        place.setPhone(updated.getPhone());
        place.setWebsite(updated.getWebsite());
        place.setEmail(updated.getEmail());
        place.setBookingUrl(updated.getBookingUrl());
        place.setBookingLabel(updated.getBookingLabel());
        place.setLatitude(updated.getLatitude());
        place.setLongitude(updated.getLongitude());
        place.setMainImage(updated.getMainImage());
        place.setOpeningHours(updated.getOpeningHours());
        place.setPriceRange(updated.getPriceRange());
        place.setTags(updated.getTags());
        place.setFeatured(updated.isFeatured());
        place.setUpdatedAt(LocalDateTime.now());
        return placeRepository.save(place);
    }

    public void toggleStatus(String id) {
        Place place = placeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Lugar no encontrado"));
        place.setActive(!place.isActive());
        place.setUpdatedAt(LocalDateTime.now());
        placeRepository.save(place);
        cityService.updateCityPlaceCount(place.getCityId());
    }

    public void deletePlace(String id) {
        placeRepository.findById(id).ifPresent(p -> {
            placeRepository.deleteById(id);
            if (p.getCityId() != null) cityService.updateCityPlaceCount(p.getCityId());
        });
    }

    public void updateAverageRating(String placeId) {
        Double avg = reviewRepository.calculateAverageRating(placeId);
        long count = reviewRepository.countByPlaceId(placeId);
        placeRepository.findById(placeId).ifPresent(place -> {
            place.setAverageRating(avg != null ? avg : 0.0);
            place.setTotalReviews((int) count);
            place.setUpdatedAt(LocalDateTime.now());
            placeRepository.save(place);
        });
    }

    public Map<String, Long> getStatsByCategory() {
        return java.util.Arrays.stream(Place.Category.values())
                .collect(Collectors.toMap(
                        Place.Category::getDisplayName,
                        cat -> placeRepository.countByCategory(cat)
                ));
    }

    public long getTotalActivePlaces()  { return placeRepository.countByActiveTrue(); }
    public long getTotalFeaturedPlaces(){ return placeRepository.countByFeaturedTrue(); }
    public List<Place> getAllPlaces()   { return placeRepository.findAll(); }
}
