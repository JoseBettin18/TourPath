package com.tourpath.service;

import com.tourpath.model.Place;
import com.tourpath.repository.PlaceRepository;
import com.tourpath.repository.ReviewRepository;
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
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("PlaceService - Tests unitarios")
class PlaceServiceTest {

    @Mock private PlaceRepository placeRepository;
    @Mock private ReviewRepository reviewRepository;
    @Mock private CityService cityService;
    @InjectMocks private PlaceService placeService;

    private Place samplePlace;

    @BeforeEach
    void setUp() {
        samplePlace = Place.builder()
                .id("place-001")
                .name("Ciudad Amurallada")
                .description("Patrimonio UNESCO")
                .category(Place.Category.SITIO_TURISTICO)
                .cityId("city-001")
                .cityName("Cartagena")
                .countryName("Colombia")
                .active(true)
                .featured(true)
                .averageRating(4.9)
                .totalReviews(100)
                .bookingUrl("https://tourismo.cartagena.com")
                .bookingLabel("Ver tours")
                .build();
    }

    @Test
    @DisplayName("Debe retornar todos los lugares activos ordenados")
    void getAllActivePlaces_returnsOrdered() {
        when(placeRepository.findByActiveTrueOrderByFeaturedDescAverageRatingDesc())
                .thenReturn(List.of(samplePlace));

        List<Place> result = placeService.getAllActivePlaces();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).isFeatured()).isTrue();
    }

    @Test
    @DisplayName("Debe filtrar lugares por ciudad")
    void getPlacesByCity_returnsFiltered() {
        when(placeRepository.findByCityIdAndActiveTrue("city-001"))
                .thenReturn(List.of(samplePlace));

        List<Place> result = placeService.getPlacesByCity("city-001");

        assertThat(result).allMatch(p -> p.getCityId().equals("city-001"));
    }

    @Test
    @DisplayName("Debe filtrar por ciudad y categoría")
    void getPlacesByCityAndCategory_returnsFiltered() {
        when(placeRepository.findByCityIdAndCategoryAndActiveTrue(
                "city-001", Place.Category.RESTAURANTE)).thenReturn(List.of());
        when(placeRepository.findByCityIdAndCategoryAndActiveTrue(
                "city-001", Place.Category.SITIO_TURISTICO)).thenReturn(List.of(samplePlace));

        List<Place> result = placeService.getPlacesByCityAndCategory(
                "city-001", Place.Category.SITIO_TURISTICO);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCategory()).isEqualTo(Place.Category.SITIO_TURISTICO);
    }

    @Test
    @DisplayName("Debe retornar lugar por ID")
    void getPlaceById_found() {
        when(placeRepository.findById("place-001")).thenReturn(Optional.of(samplePlace));

        Optional<Place> result = placeService.getPlaceById("place-001");

        assertThat(result).isPresent();
        assertThat(result.get().getBookingUrl()).isNotNull();
    }

    @Test
    @DisplayName("Debe guardar lugar nuevo con timestamp y active=true")
    void savePlace_newPlace_setsFields() {
        Place newPlace = Place.builder()
                .name("Nuevo Lugar")
                .description("Descripción")
                .category(Place.Category.RESTAURANTE)
                .cityId("city-001")
                .build();

        when(placeRepository.save(any(Place.class))).thenReturn(newPlace);
        doNothing().when(cityService).updateCityPlaceCount(anyString());

        Place result = placeService.savePlace(newPlace);

        assertThat(result).isNotNull();
        assertThat(newPlace.isActive()).isTrue();
        assertThat(newPlace.getCreatedAt()).isNotNull();
        verify(placeRepository).save(any(Place.class));
        verify(cityService).updateCityPlaceCount("city-001");
    }

    @Test
    @DisplayName("Debe lanzar excepción al actualizar lugar inexistente")
    void updatePlace_notFound_throwsException() {
        when(placeRepository.findById("no-existe")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> placeService.updatePlace("no-existe", samplePlace))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("no encontrado");
    }

    @Test
    @DisplayName("Debe actualizar booking URL correctamente")
    void updatePlace_updatesBookingUrl() {
        Place updated = Place.builder()
                .name("Ciudad Amurallada Actualizada")
                .description("Nueva descripción")
                .category(Place.Category.SITIO_TURISTICO)
                .bookingUrl("https://nueva-reserva.com")
                .bookingLabel("Nueva reserva")
                .build();

        when(placeRepository.findById("place-001")).thenReturn(Optional.of(samplePlace));
        when(placeRepository.save(any(Place.class))).thenReturn(samplePlace);

        Place result = placeService.updatePlace("place-001", updated);

        assertThat(samplePlace.getBookingUrl()).isEqualTo("https://nueva-reserva.com");
        assertThat(samplePlace.getBookingLabel()).isEqualTo("Nueva reserva");
    }

    @Test
    @DisplayName("Debe alternar estado activo del lugar")
    void toggleStatus_invertsActive() {
        samplePlace.setActive(true);
        when(placeRepository.findById("place-001")).thenReturn(Optional.of(samplePlace));
        when(placeRepository.save(any(Place.class))).thenReturn(samplePlace);
        doNothing().when(cityService).updateCityPlaceCount(anyString());

        placeService.toggleStatus("place-001");

        assertThat(samplePlace.isActive()).isFalse();
        verify(placeRepository).save(samplePlace);
    }

    @Test
    @DisplayName("Debe retornar estadísticas por categoría")
    void getStatsByCategory_returnsMap() {
        when(placeRepository.countByCategory(any(Place.Category.class))).thenReturn(2L);

        var stats = placeService.getStatsByCategory();

        assertThat(stats).isNotEmpty();
        assertThat(stats.values()).allMatch(v -> v >= 0);
    }
}
