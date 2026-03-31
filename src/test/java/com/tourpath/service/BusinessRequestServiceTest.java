package com.tourpath.service;

import com.tourpath.model.BusinessRequest;
import com.tourpath.model.User;
import com.tourpath.repository.BusinessRequestRepository;
import com.tourpath.repository.PlaceRepository;
import com.tourpath.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("BusinessRequestService - Tests unitarios")
class BusinessRequestServiceTest {

    @Mock private BusinessRequestRepository requestRepository;
    @Mock private PlaceRepository           placeRepository;
    @Mock private UserRepository            userRepository;
    @Mock private CityService               cityService;
    @InjectMocks private BusinessRequestService service;

    private BusinessRequest sampleRequest;

    @BeforeEach
    void setUp() {
        sampleRequest = BusinessRequest.builder()
                .id("req-001")
                .ownerName("Carlos Dueño")
                .ownerEmail("carlos@negocio.com")
                .ownerPhone("+57 300 000 0000")
                .businessName("Restaurante El Caribe")
                .businessDescription("El mejor restaurante caribeño de la ciudad")
                .businessCategory("RESTAURANTE")
                .businessCityId("city-001")
                .businessCityName("Cartagena")
                .businessCountry("Colombia")
                .businessBookingUrl("https://wa.me/573000000000")
                .businessBookingLabel("Reservar por WhatsApp")
                .status(BusinessRequest.Status.PENDING)
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Test
    @DisplayName("Debe enviar solicitud con estado PENDING")
    void submitRequest_setsPendingStatus() {
        when(requestRepository.save(any(BusinessRequest.class))).thenReturn(sampleRequest);

        BusinessRequest result = service.submitRequest(sampleRequest);

        assertThat(result.getStatus()).isEqualTo(BusinessRequest.Status.PENDING);
        assertThat(result.getCreatedAt()).isNotNull();
        verify(requestRepository).save(sampleRequest);
    }

    @Test
    @DisplayName("Debe retornar todas las solicitudes ordenadas")
    void getAllRequests_returnsAll() {
        when(requestRepository.findAllByOrderByCreatedAtDesc())
                .thenReturn(List.of(sampleRequest));

        List<BusinessRequest> result = service.getAllRequests();

        assertThat(result).hasSize(1);
    }

    @Test
    @DisplayName("Debe retornar solo solicitudes pendientes")
    void getPendingRequests_returnsOnlyPending() {
        when(requestRepository.findByStatusOrderByCreatedAtDesc(BusinessRequest.Status.PENDING))
                .thenReturn(List.of(sampleRequest));

        List<BusinessRequest> result = service.getPendingRequests();

        assertThat(result).allMatch(r -> r.getStatus() == BusinessRequest.Status.PENDING);
    }

    @Test
    @DisplayName("Debe contar solicitudes pendientes")
    void countPending_returnsCount() {
        when(requestRepository.countByStatus(BusinessRequest.Status.PENDING)).thenReturn(3L);

        assertThat(service.countPending()).isEqualTo(3L);
    }

    @Test
    @DisplayName("Debe rechazar solicitud con motivo y revisor")
    void rejectRequest_setsRejectedStatus() {
        when(requestRepository.findById("req-001")).thenReturn(Optional.of(sampleRequest));
        when(requestRepository.save(any(BusinessRequest.class))).thenReturn(sampleRequest);

        service.rejectRequest("req-001", "Información incompleta", "superadmin");

        assertThat(sampleRequest.getStatus()).isEqualTo(BusinessRequest.Status.REJECTED);
        assertThat(sampleRequest.getRejectionReason()).isEqualTo("Información incompleta");
        assertThat(sampleRequest.getReviewedBy()).isEqualTo("superadmin");
        assertThat(sampleRequest.getReviewedAt()).isNotNull();
        verify(requestRepository).save(sampleRequest);
    }

    @Test
    @DisplayName("Debe lanzar excepción al rechazar solicitud inexistente")
    void rejectRequest_notFound_throwsException() {
        when(requestRepository.findById("no-existe")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.rejectRequest("no-existe", "motivo", "admin"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Solicitud no encontrada");
    }

    @Test
    @DisplayName("Debe aprobar solicitud creando el lugar y el owner")
    void approveRequest_createsPlaceAndOwner() {
        when(requestRepository.findById("req-001")).thenReturn(Optional.of(sampleRequest));
        when(userRepository.findByEmail("carlos@negocio.com")).thenReturn(Optional.empty());
        when(userRepository.existsByUsername(anyString())).thenReturn(false);

        User savedOwner = User.builder()
                .id("owner-001").username("carlos_owner").email("carlos@negocio.com")
                .roles(Set.of(User.Role.ROLE_OWNER, User.Role.ROLE_USER))
                .enabled(true).accountNonLocked(true).build();

        com.tourpath.model.Place savedPlace = com.tourpath.model.Place.builder()
                .id("place-new").name("Restaurante El Caribe").active(true).build();

        when(placeRepository.save(any())).thenReturn(savedPlace);
        when(userRepository.save(any())).thenReturn(savedOwner);
        when(requestRepository.save(any())).thenReturn(sampleRequest);
        doNothing().when(cityService).updateCityPlaceCount(anyString());

        service.approveRequest("req-001", "superadmin");

        assertThat(sampleRequest.getStatus()).isEqualTo(BusinessRequest.Status.APPROVED);
        assertThat(sampleRequest.getReviewedBy()).isEqualTo("superadmin");
        verify(placeRepository, atLeastOnce()).save(any());
        verify(userRepository, atLeastOnce()).save(any());
    }
}
