package com.tourpath.controller;

import com.tourpath.model.BusinessRequest;
import com.tourpath.model.Place;
import com.tourpath.service.BusinessRequestService;
import com.tourpath.service.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/business")
public class BusinessController {

    @Autowired private BusinessRequestService businessRequestService;
    @Autowired private CityService cityService;

    /** Formulario público de solicitud de registro */
    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("request", new BusinessRequest());
        model.addAttribute("cities", cityService.getAllActiveCities());
        model.addAttribute("categories", Place.Category.values());
        model.addAttribute("pageTitle", "Registra tu establecimiento | TourPath");
        return "business/register";
    }

    @PostMapping("/register")
    public String submitRegister(@ModelAttribute("request") BusinessRequest request,
                                 BindingResult result,
                                 Authentication auth,
                                 RedirectAttributes ra,
                                 Model model) {
        if (request.getOwnerName() == null || request.getOwnerName().isBlank() ||
            request.getOwnerEmail() == null || request.getOwnerEmail().isBlank() ||
            request.getBusinessName() == null || request.getBusinessName().isBlank() ||
            request.getBusinessDescription() == null || request.getBusinessDescription().isBlank()) {
            model.addAttribute("error", "Completa todos los campos obligatorios.");
            model.addAttribute("cities", cityService.getAllActiveCities());
            model.addAttribute("categories", Place.Category.values());
            return "business/register";
        }

        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
            request.setRequestingUserId(auth.getName());
        }

        businessRequestService.submitRequest(request);
        ra.addFlashAttribute("success",
            "✅ ¡Solicitud enviada! Nuestro equipo la revisará en los próximos días.");
        return "redirect:/business/register?sent=true";
    }

    @GetMapping("/status")
    public String requestStatus(Model model) {
        model.addAttribute("pageTitle", "Estado de tu solicitud | TourPath");
        return "business/status";
    }
}
