package com.tourpath.service;

import com.tourpath.model.City;
import com.tourpath.repository.CityRepository;
import com.tourpath.repository.PlaceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CityService - Tests unitarios")
class CityServiceTest {

    @Mock private CityRepository cityRepository;
    @Mock private PlaceRepository placeRepository;
    @InjectMocks private CityService cityService;

    private City sampleCity;

    @BeforeEach
    void setUp() {
        sampleCity = City.builder()
                .id("city-001")
                .name("Cartagena")
                .country("Colombia")
                .countryCode("CO")
                .continent("América del Sur")
                .flagEmoji("🇨🇴")
                .active(true)
                .featured(true)
                .build();
    }

    @Test
    @DisplayName("Debe retornar todas las ciudades activas ordenadas")
    void getAllActiveCities_returnsOrdered() {
        when(cityRepository.findByActiveTrueOrderByFeaturedDescNameAsc())
                .thenReturn(List.of(sampleCity));

        List<City> result = cityService.getAllActiveCities();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Cartagena");
        verify(cityRepository).findByActiveTrueOrderByFeaturedDescNameAsc();
    }

    @Test
    @DisplayName("Debe retornar ciudades destacadas")
    void getFeaturedCities_returnsOnlyFeatured() {
        when(cityRepository.findByFeaturedTrueAndActiveTrue())
                .thenReturn(List.of(sampleCity));

        List<City> result = cityService.getFeaturedCities();

        assertThat(result).allMatch(City::isFeatured);
    }

    @Test
    @DisplayName("Debe retornar ciudad por ID cuando existe")
    void getCityById_found() {
        when(cityRepository.findById("city-001")).thenReturn(Optional.of(sampleCity));

        Optional<City> result = cityService.getCityById("city-001");

        assertThat(result).isPresent();
        assertThat(result.get().getCountryCode()).isEqualTo("CO");
    }

    @Test
    @DisplayName("Debe retornar vacío cuando la ciudad no existe")
    void getCityById_notFound() {
        when(cityRepository.findById("no-existe")).thenReturn(Optional.empty());

        Optional<City> result = cityService.getCityById("no-existe");

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Debe guardar una ciudad y asignar timestamp")
    void saveCity_newCity_savesCorrectly() {
        City newCity = City.builder().name("Bogotá").country("Colombia").build();
        when(cityRepository.save(any(City.class))).thenReturn(newCity);

        City result = cityService.saveCity(newCity);

        assertThat(result).isNotNull();
        assertThat(result.getCreatedAt()).isNotNull();
        verify(cityRepository).save(any(City.class));
    }

    @Test
    @DisplayName("Debe buscar ciudades por nombre")
    void searchCities_returnsMatches() {
        when(cityRepository.searchByName("carta")).thenReturn(List.of(sampleCity));

        List<City> result = cityService.searchCities("carta");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).containsIgnoringCase("carta");
    }

    @Test
    @DisplayName("Debe actualizar el contador de lugares de una ciudad")
    void updateCityPlaceCount_updatesCorrectly() {
        when(cityRepository.findById("city-001")).thenReturn(Optional.of(sampleCity));
        when(placeRepository.countByCityIdAndActiveTrue("city-001")).thenReturn(5L);
        when(cityRepository.save(any(City.class))).thenReturn(sampleCity);

        cityService.updateCityPlaceCount("city-001");

        assertThat(sampleCity.getTotalPlaces()).isEqualTo(5);
        verify(cityRepository).save(sampleCity);
    }

    @Test
    @DisplayName("Debe contar el total de ciudades")
    void countCities_returnsTotal() {
        when(cityRepository.count()).thenReturn(10L);

        assertThat(cityService.countCities()).isEqualTo(10L);
    }
}
