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
public class HomeController {

    @Autowired private CityService cityService;
    @Autowired private PlaceService placeService;

    @GetMapping({"/", "/home"})
    public String home(Model model) {
        model.addAttribute("featuredCities", cityService.getFeaturedCities());
        model.addAttribute("featuredPlaces", placeService.getFeaturedPlaces().stream().limit(6).toList());
        model.addAttribute("totalCities", cityService.countCities());
        model.addAttribute("totalPlaces", placeService.getTotalActivePlaces());
        return "home";
    }

    @GetMapping("/explore")
    public String explore(@RequestParam(required = false) String continent,
                          @RequestParam(required = false) String country,
                          @RequestParam(required = false) String q,
                          Model model) {
        List<City> cities;
        if (q != null && !q.isBlank()) {
            cities = cityService.searchCities(q);
        } else if (continent != null && !continent.isBlank()) {
            cities = cityService.getCitiesByContinent(continent);
        } else if (country != null && !country.isBlank()) {
            cities = cityService.getCitiesByCountry(country);
        } else {
            cities = cityService.getAllActiveCities();
        }
        model.addAttribute("cities", cities);
        model.addAttribute("query", q);
        model.addAttribute("selectedContinent", continent);
        model.addAttribute("selectedCountry", country);
        return "explore";
    }

    @GetMapping("/search")
    public String search(@RequestParam(required = false) String q,
                         @RequestParam(required = false) String category,
                         Model model) {
        List<Place> places;
        if (q != null && !q.isBlank()) {
            places = placeService.searchPlaces(q);
        } else if (category != null && !category.isBlank()) {
            try {
                Place.Category cat = Place.Category.valueOf(category);
                places = placeService.getAllActivePlaces().stream()
                        .filter(p -> p.getCategory() == cat).toList();
            } catch (IllegalArgumentException e) {
                places = placeService.getAllActivePlaces();
            }
        } else {
            places = placeService.getAllActivePlaces();
        }
        model.addAttribute("places", places);
        model.addAttribute("query", q);
        model.addAttribute("selectedCategory", category);
        model.addAttribute("categories", Place.Category.values());
        return "search";
    }

    @GetMapping("/nosotros")
    public String about() { return "nosotros"; }

    @GetMapping("/contacto")
    public String contact() { return "contacto"; }
}
