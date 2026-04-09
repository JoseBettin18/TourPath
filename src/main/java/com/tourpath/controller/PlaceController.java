package com.tourpath.controller;

import com.tourpath.model.Place;
import com.tourpath.model.Review;
import com.tourpath.service.PlaceService;
import com.tourpath.service.ReviewService;
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
@RequestMapping("/places")
public class PlaceController {

    @Autowired
    private PlaceService placeService;
    @Autowired
    private ReviewService reviewService;

    @GetMapping("/{id}")
    public String placeDetail(@PathVariable String id, Model model) {
        Place place = placeService.getPlaceById(id)
                .orElseThrow(() -> new RuntimeException("Lugar no encontrado"));

        model.addAttribute("place", place);
        model.addAttribute("reviews", reviewService.getReviewsByPlace(id));
        model.addAttribute("newReview", new Review());
        model.addAttribute("pageTitle", place.getName() + " | TourPath");
        return "places/detail";
    }

    @PostMapping("/{id}/review")
    @PreAuthorize("isAuthenticated()")
    public String addReview(@PathVariable String id,
            @Valid @ModelAttribute("newReview") Review review,
            BindingResult result,
            Authentication auth,
            RedirectAttributes ra) {
        if (result.hasErrors()) {
            ra.addFlashAttribute("reviewError", "Verifica los campos de la reseña.");
            return "redirect:/places/" + id;
        }
        review.setPlaceId(id);
        review.setUserId(auth.getName());
        review.setUsername(auth.getName());
        reviewService.addReview(review);
        ra.addFlashAttribute("success", "¡Reseña publicada!");
        return "redirect:/places/" + id;
    }
}
