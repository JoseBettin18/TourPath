package com.tourpath.controller;

import com.tourpath.model.Place;
import com.tourpath.model.User;
import com.tourpath.service.PlaceService;
import com.tourpath.service.ReviewService;
import com.tourpath.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/owner")
@PreAuthorize("hasAnyRole('OWNER','SUPER_ADMIN')")
public class OwnerController {

    @Autowired private PlaceService placeService;
    @Autowired private UserService userService;
    @Autowired private ReviewService reviewService;

    /** Dashboard del dueño */
    @GetMapping("/dashboard")
    public String dashboard(Authentication auth, Model model) {
        User owner = userService.getUserByUsername(auth.getName())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Place myPlace = null;
        if (owner.getOwnedPlaceId() != null) {
            myPlace = placeService.getPlaceById(owner.getOwnedPlaceId()).orElse(null);
        }

        model.addAttribute("owner", owner);
        model.addAttribute("myPlace", myPlace);
        model.addAttribute("reviews", myPlace != null
                ? reviewService.getReviewsByPlace(myPlace.getId()) : java.util.List.of());
        return "owner/dashboard";
    }

    /** Formulario de edición del establecimiento */
    @GetMapping("/place/edit")
    public String editPlaceForm(Authentication auth, Model model) {
        User owner = userService.getUserByUsername(auth.getName())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (owner.getOwnedPlaceId() == null) return "redirect:/owner/dashboard";

        Place place = placeService.getPlaceById(owner.getOwnedPlaceId())
                .orElseThrow(() -> new RuntimeException("Establecimiento no encontrado"));

        model.addAttribute("place", place);
        model.addAttribute("categories", Place.Category.values());
        return "owner/edit-place";
    }

    /** Guardar cambios del establecimiento */
    @PostMapping("/place/edit")
    public String updatePlace(Authentication auth,
                              @Valid @ModelAttribute("place") Place updatedPlace,
                              BindingResult result,
                              RedirectAttributes ra,
                              Model model) {
        if (result.hasErrors()) {
            model.addAttribute("categories", Place.Category.values());
            return "owner/edit-place";
        }

        User owner = userService.getUserByUsername(auth.getName())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (owner.getOwnedPlaceId() == null) return "redirect:/owner/dashboard";

        // Seguridad: el owner solo puede editar SU establecimiento
        Place current = placeService.getPlaceById(owner.getOwnedPlaceId())
                .orElseThrow(() -> new RuntimeException("Establecimiento no encontrado"));

        if (!current.getId().equals(updatedPlace.getId())) {
            ra.addFlashAttribute("error", "No autorizado.");
            return "redirect:/owner/dashboard";
        }

        placeService.updatePlace(current.getId(), updatedPlace);
        ra.addFlashAttribute("success", "✅ Información actualizada correctamente.");
        return "redirect:/owner/dashboard";
    }
}
