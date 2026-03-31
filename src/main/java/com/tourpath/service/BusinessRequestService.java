package com.tourpath.service;

import com.tourpath.model.BusinessRequest;
import com.tourpath.model.Place;
import com.tourpath.model.User;
import com.tourpath.repository.BusinessRequestRepository;
import com.tourpath.repository.PlaceRepository;
import com.tourpath.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class BusinessRequestService {

    @Autowired private BusinessRequestRepository requestRepository;
    @Autowired private PlaceRepository           placeRepository;
    @Autowired private UserRepository            userRepository;
    @Autowired private CityService               cityService;

    public BusinessRequest submitRequest(BusinessRequest request) {
        request.setStatus(BusinessRequest.Status.PENDING);
        request.setCreatedAt(LocalDateTime.now());
        return requestRepository.save(request);
    }

    public List<BusinessRequest> getAllRequests() {
        return requestRepository.findAllByOrderByCreatedAtDesc();
    }

    public List<BusinessRequest> getPendingRequests() {
        return requestRepository.findByStatusOrderByCreatedAtDesc(BusinessRequest.Status.PENDING);
    }

    public Optional<BusinessRequest> getRequestById(String id) {
        return requestRepository.findById(id);
    }

    public long countPending() {
        return requestRepository.countByStatus(BusinessRequest.Status.PENDING);
    }

    /**
     * Aprobar solicitud:
     * 1. Crear el Place en la BD
     * 2. Crear cuenta OWNER para el solicitante (o promover si ya existe)
     * 3. Marcar solicitud como APPROVED
     */
    public void approveRequest(String requestId, String reviewerUsername) {
        BusinessRequest req = requestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Solicitud no encontrada"));

        // ── 1. Crear el Place ─────────────────────────────────────
        Place.Category category = parseCategoryOrDefault(req.getBusinessCategory());

        Place place = new Place();
        place.setName(req.getBusinessName());
        place.setDescription(req.getBusinessDescription());
        place.setCategory(category);
        place.setCityId(req.getBusinessCityId());
        place.setCityName(req.getBusinessCityName());
        place.setCountryName(req.getBusinessCountry());
        place.setAddress(req.getBusinessAddress());
        place.setPhone(req.getBusinessPhone());
        place.setEmail(req.getBusinessEmail());
        place.setWebsite(req.getBusinessWebsite());
        place.setBookingUrl(req.getBusinessBookingUrl());
        place.setBookingLabel(req.getBusinessBookingLabel() != null
                ? req.getBusinessBookingLabel() : "Reservar ahora");
        place.setMainImage(req.getBusinessImage());
        place.setOpeningHours(req.getOpeningHours());
        place.setPriceRange(req.getPriceRange());
        place.setOwnerName(req.getOwnerName());
        place.setActive(true);
        place.setFeatured(false);
        place.setAverageRating(0.0);
        place.setTotalReviews(0);
        place.setCreatedAt(LocalDateTime.now());
        place.setUpdatedAt(LocalDateTime.now());

        // ── 2. Crear/promover usuario OWNER ───────────────────────
        Optional<User> existingUser = userRepository.findByEmail(req.getOwnerEmail());
        User owner;

        if (existingUser.isPresent()) {
            owner = existingUser.get();
            owner.getRoles().add(User.Role.ROLE_OWNER);
            owner.setUpdatedAt(LocalDateTime.now());
        } else {
            // Generar username desde el email
            String username = req.getOwnerEmail().split("@")[0]
                    .replaceAll("[^a-zA-Z0-9]", "") + "_owner";
            // Asegurar que sea único
            if (userRepository.existsByUsername(username)) username += System.currentTimeMillis() % 1000;

            owner = User.builder()
                    .username(username)
                    .email(req.getOwnerEmail())
                    .password("$2a$10$TEMP_CHANGE_ON_FIRST_LOGIN") // owner debe cambiar contraseña
                    .fullName(req.getOwnerName())
                    .phone(req.getOwnerPhone())
                    .roles(Set.of(User.Role.ROLE_OWNER, User.Role.ROLE_USER))
                    .enabled(true)
                    .accountNonLocked(true)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();
        }

        place.setOwnerId(owner.getId() != null ? owner.getId() : "pending");
        Place savedPlace = placeRepository.save(place);

        owner.setOwnedPlaceId(savedPlace.getId());
        User savedOwner = userRepository.save(owner);

        // Actualizar el ownerId en el place con el id real del owner
        savedPlace.setOwnerId(savedOwner.getId());
        placeRepository.save(savedPlace);

        // Actualizar contador de ciudad
        if (req.getBusinessCityId() != null) {
            cityService.updateCityPlaceCount(req.getBusinessCityId());
        }

        // ── 3. Marcar solicitud como aprobada ─────────────────────
        req.setStatus(BusinessRequest.Status.APPROVED);
        req.setReviewedBy(reviewerUsername);
        req.setReviewedAt(LocalDateTime.now());
        req.setRequestingUserId(savedOwner.getId());
        requestRepository.save(req);
    }

    /**
     * Rechazar solicitud con motivo
     */
    public void rejectRequest(String requestId, String reason, String reviewerUsername) {
        BusinessRequest req = requestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Solicitud no encontrada"));
        req.setStatus(BusinessRequest.Status.REJECTED);
        req.setRejectionReason(reason);
        req.setReviewedBy(reviewerUsername);
        req.setReviewedAt(LocalDateTime.now());
        requestRepository.save(req);
    }

    private Place.Category parseCategoryOrDefault(String cat) {
        if (cat == null) return Place.Category.ENTRETENIMIENTO;
        try {
            return Place.Category.valueOf(cat);
        } catch (IllegalArgumentException e) {
            return Place.Category.ENTRETENIMIENTO;
        }
    }
}
