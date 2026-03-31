package com.tourpath.controller;

import com.tourpath.model.*;
import com.tourpath.service.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.Set;

@Controller
@RequestMapping("/superadmin")
@PreAuthorize("hasRole('SUPER_ADMIN')")
public class SuperAdminController {

    @Autowired private PlaceService placeService;
    @Autowired private CityService cityService;
    @Autowired private UserService userService;
    @Autowired private ReviewService reviewService;
    @Autowired private BusinessRequestService businessRequestService;

    // ── Dashboard ─────────────────────────────────────────────────
    @GetMapping({"/", "/dashboard"})
    public String dashboard(Model model) {
        model.addAttribute("totalPlaces",   placeService.getTotalActivePlaces());
        model.addAttribute("totalCities",   cityService.countCities());
        model.addAttribute("totalUsers",    userService.countUsers());
        model.addAttribute("pendingRequests", businessRequestService.countPending());
        model.addAttribute("featuredPlaces", placeService.getTotalFeaturedPlaces());
        model.addAttribute("statsByCategory", placeService.getStatsByCategory());
        model.addAttribute("recentRequests", businessRequestService.getPendingRequests()
                .stream().limit(5).toList());
        model.addAttribute("recentPlaces", placeService.getAllActivePlaces()
                .stream().limit(5).toList());
        return "admin/dashboard";
    }

    // ── Solicitudes de negocios ───────────────────────────────────
    @GetMapping("/requests")
    public String manageRequests(Model model) {
        model.addAttribute("pendingRequests",
                businessRequestService.getPendingRequests());
        model.addAttribute("allRequests",
                businessRequestService.getAllRequests());
        model.addAttribute("pendingCount", businessRequestService.countPending());
        return "admin/requests";
    }

    @GetMapping("/requests/{id}")
    public String requestDetail(@PathVariable String id, Model model) {
        BusinessRequest req = businessRequestService.getRequestById(id)
                .orElseThrow(() -> new RuntimeException("Solicitud no encontrada"));
        model.addAttribute("request", req);
        return "admin/request-detail";
    }

    @PostMapping("/requests/{id}/approve")
    public String approveRequest(@PathVariable String id,
                                 Authentication auth,
                                 RedirectAttributes ra) {
        businessRequestService.approveRequest(id, auth.getName());
        ra.addFlashAttribute("success", "✅ Solicitud aprobada. Establecimiento creado.");
        return "redirect:/superadmin/requests";
    }

    @PostMapping("/requests/{id}/reject")
    public String rejectRequest(@PathVariable String id,
                                @RequestParam String reason,
                                Authentication auth,
                                RedirectAttributes ra) {
        businessRequestService.rejectRequest(id, reason, auth.getName());
        ra.addFlashAttribute("success", "Solicitud rechazada.");
        return "redirect:/superadmin/requests";
    }

    // ── Ciudades ──────────────────────────────────────────────────
    @GetMapping("/cities")
    public String manageCities(Model model) {
        model.addAttribute("cities", cityService.getAllCities());
        return "admin/cities";
    }

    @GetMapping("/cities/new")
    public String newCityForm(Model model) {
        model.addAttribute("city", new City());
        model.addAttribute("action", "Crear");
        return "admin/city-form";
    }

    @PostMapping("/cities/new")
    public String createCity(@Valid @ModelAttribute("city") City city,
                             BindingResult result,
                             Model model,
                             RedirectAttributes ra) {
        if (result.hasErrors()) { model.addAttribute("action", "Crear"); return "admin/city-form"; }
        city.setActive(true);
        cityService.saveCity(city);
        ra.addFlashAttribute("success", "✅ Ciudad creada.");
        return "redirect:/superadmin/cities";
    }

    @GetMapping("/cities/{id}/edit")
    public String editCityForm(@PathVariable String id, Model model) {
        model.addAttribute("city", cityService.getCityById(id)
                .orElseThrow(() -> new RuntimeException("Ciudad no encontrada")));
        model.addAttribute("action", "Editar");
        return "admin/city-form";
    }

    @PostMapping("/cities/{id}/edit")
    public String updateCity(@PathVariable String id,
                             @Valid @ModelAttribute("city") City city,
                             BindingResult result,
                             Model model,
                             RedirectAttributes ra) {
        if (result.hasErrors()) { model.addAttribute("action", "Editar"); return "admin/city-form"; }
        city.setId(id);
        cityService.saveCity(city);
        ra.addFlashAttribute("success", "✅ Ciudad actualizada.");
        return "redirect:/superadmin/cities";
    }

    @PostMapping("/cities/{id}/delete")
    public String deleteCity(@PathVariable String id, RedirectAttributes ra) {
        cityService.deleteCity(id);
        ra.addFlashAttribute("success", "Ciudad eliminada.");
        return "redirect:/superadmin/cities";
    }

    // ── Lugares ───────────────────────────────────────────────────
    @GetMapping("/places")
    public String managePlaces(Model model) {
        model.addAttribute("places", placeService.getAllPlaces());
        return "admin/places";
    }

    @GetMapping("/places/new")
    public String newPlaceForm(Model model) {
        model.addAttribute("place", new Place());
        model.addAttribute("cities", cityService.getAllActiveCities());
        model.addAttribute("categories", Place.Category.values());
        model.addAttribute("action", "Crear");
        return "admin/place-form";
    }

    @PostMapping("/places/new")
    public String createPlace(@Valid @ModelAttribute("place") Place place,
                              BindingResult result,
                              Model model,
                              RedirectAttributes ra) {
        if (result.hasErrors()) {
            model.addAttribute("cities", cityService.getAllActiveCities());
            model.addAttribute("categories", Place.Category.values());
            model.addAttribute("action", "Crear");
            return "admin/place-form";
        }
        cityService.getCityById(place.getCityId()).ifPresent(city -> {
            place.setCityName(city.getName());
            place.setCountryName(city.getCountry());
        });
        placeService.savePlace(place);
        ra.addFlashAttribute("success", "✅ Lugar creado.");
        return "redirect:/superadmin/places";
    }

    @GetMapping("/places/{id}/edit")
    public String editPlaceForm(@PathVariable String id, Model model) {
        model.addAttribute("place", placeService.getPlaceById(id)
                .orElseThrow(() -> new RuntimeException("Lugar no encontrado")));
        model.addAttribute("cities", cityService.getAllActiveCities());
        model.addAttribute("categories", Place.Category.values());
        model.addAttribute("action", "Editar");
        return "admin/place-form";
    }

    @PostMapping("/places/{id}/edit")
    public String updatePlace(@PathVariable String id,
                              @Valid @ModelAttribute("place") Place place,
                              BindingResult result,
                              Model model,
                              RedirectAttributes ra) {
        if (result.hasErrors()) {
            model.addAttribute("cities", cityService.getAllActiveCities());
            model.addAttribute("categories", Place.Category.values());
            model.addAttribute("action", "Editar");
            return "admin/place-form";
        }
        placeService.updatePlace(id, place);
        ra.addFlashAttribute("success", "✅ Lugar actualizado.");
        return "redirect:/superadmin/places";
    }

    @PostMapping("/places/{id}/toggle")
    public String togglePlace(@PathVariable String id, RedirectAttributes ra) {
        placeService.toggleStatus(id);
        ra.addFlashAttribute("success", "Estado actualizado.");
        return "redirect:/superadmin/places";
    }

    @PostMapping("/places/{id}/featured")
    public String toggleFeatured(@PathVariable String id, RedirectAttributes ra) {
        placeService.getPlaceById(id).ifPresent(p -> {
            p.setFeatured(!p.isFeatured());
            placeService.savePlace(p);
        });
        ra.addFlashAttribute("success", "Estado destacado actualizado.");
        return "redirect:/superadmin/places";
    }

    @PostMapping("/places/{id}/delete")
    public String deletePlace(@PathVariable String id, RedirectAttributes ra) {
        placeService.deletePlace(id);
        ra.addFlashAttribute("success", "Lugar eliminado.");
        return "redirect:/superadmin/places";
    }

    // ── Usuarios ──────────────────────────────────────────────────
    @GetMapping("/users")
    public String manageUsers(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "admin/users";
    }

    @PostMapping("/users/{id}/toggle")
    public String toggleUser(@PathVariable String id, RedirectAttributes ra) {
        userService.toggleStatus(id);
        ra.addFlashAttribute("success", "Estado de usuario actualizado.");
        return "redirect:/superadmin/users";
    }

    @PostMapping("/users/{id}/makeSuperAdmin")
    public String makeSuperAdmin(@PathVariable String id, RedirectAttributes ra) {
        userService.updateRoles(id, Set.of(User.Role.ROLE_SUPER_ADMIN, User.Role.ROLE_USER));
        ra.addFlashAttribute("success", "Usuario promovido a Super Admin.");
        return "redirect:/superadmin/users";
    }

    @PostMapping("/users/{id}/makeUser")
    public String makeUser(@PathVariable String id, RedirectAttributes ra) {
        userService.updateRoles(id, Set.of(User.Role.ROLE_USER));
        ra.addFlashAttribute("success", "Permisos actualizados a Usuario.");
        return "redirect:/superadmin/users";
    }

    @PostMapping("/users/{id}/delete")
    public String deleteUser(@PathVariable String id, RedirectAttributes ra) {
        userService.deleteUser(id);
        ra.addFlashAttribute("success", "Usuario eliminado.");
        return "redirect:/superadmin/users";
    }

    @PostMapping("/reviews/{id}/delete")
    public String deleteReview(@PathVariable String id, RedirectAttributes ra) {
        reviewService.deleteReview(id);
        ra.addFlashAttribute("success", "Reseña eliminada.");
        return "redirect:/superadmin/dashboard";
    }
}
