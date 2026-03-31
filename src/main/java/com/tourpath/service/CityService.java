package com.tourpath.service;

import com.tourpath.model.City;
import com.tourpath.repository.CityRepository;
import com.tourpath.repository.PlaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CityService {

    @Autowired private CityRepository cityRepository;
    @Autowired private PlaceRepository placeRepository;

    public City saveCity(City city) {
        if (city.getId() == null) city.setCreatedAt(LocalDateTime.now());
        return cityRepository.save(city);
    }

    public List<City> getAllActiveCities() {
        return cityRepository.findByActiveTrueOrderByFeaturedDescNameAsc();
    }

    public List<City> getFeaturedCities() {
        return cityRepository.findByFeaturedTrueAndActiveTrue();
    }

    public Optional<City> getCityById(String id) {
        return cityRepository.findById(id);
    }

    public List<City> searchCities(String query) {
        return cityRepository.searchByName(query);
    }

    public List<City> getCitiesByCountry(String country) {
        return cityRepository.findByCountryIgnoreCaseAndActiveTrue(country);
    }

    public List<City> getCitiesByContinent(String continent) {
        return cityRepository.findByContinentAndActiveTrue(continent);
    }

    public void updateCityPlaceCount(String cityId) {
        cityRepository.findById(cityId).ifPresent(city -> {
            long count = placeRepository.countByCityIdAndActiveTrue(cityId);
            city.setTotalPlaces((int) count);
            cityRepository.save(city);
        });
    }

    public void deleteCity(String id) { cityRepository.deleteById(id); }

    public List<City> getAllCities() { return cityRepository.findAll(); }

    public long countCities() { return cityRepository.count(); }
}
