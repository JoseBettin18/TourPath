package com.tourpath.controller;

import com.tourpath.model.City;
import com.tourpath.model.Place;
import com.tourpath.service.CityService;
import com.tourpath.service.PlaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/city")
public class CityController {

    @Autowired
    private CityService cityService;
    @Autowired
    private PlaceService placeService;

    /** Página principal de una ciudad con todos sus lugares */
    @GetMapping("/{id}")
    public String cityDetail(@PathVariable String id,
            @RequestParam(required = false) String category,
            Model model) {
        City city = cityService.getCityById(id)
                .orElseThrow(() -> new RuntimeException("Ciudad no encontrada"));

        List<Place> places;
        if (category != null && !category.isBlank()) {
            try {
                Place.Category cat = Place.Category.valueOf(category);
                places = placeService.getPlacesByCityAndCategory(id, cat);
                model.addAttribute("selectedCategory", cat);
            } catch (IllegalArgumentException e) {
                places = placeService.getPlacesByCity(id);
            }
        } else {
            places = placeService.getPlacesByCity(id);
        }

        model.addAttribute("city", city);
        model.addAttribute("places", places);
        model.addAttribute("categories", Place.Category.values());
        model.addAttribute("pageTitle", city.getName() + " | TourPath");
        return "city/detail";
    }
}
