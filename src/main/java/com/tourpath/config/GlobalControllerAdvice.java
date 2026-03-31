package com.tourpath.config;

import com.tourpath.model.Place;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.ui.Model;

@ControllerAdvice
public class GlobalControllerAdvice {

    @ModelAttribute
    public void addGlobalAttributes(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isAuth = auth != null && auth.isAuthenticated()
                && !"anonymousUser".equals(auth.getPrincipal());
        boolean isSuperAdmin = isAuth && auth != null && auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_SUPER_ADMIN"));
        boolean isOwner = isAuth && auth != null && auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_OWNER"));

        model.addAttribute("isAuthenticated", isAuth);
        model.addAttribute("isSuperAdmin", isSuperAdmin);
        model.addAttribute("isOwner", isOwner);
        model.addAttribute("currentUsername", isAuth && auth != null ? auth.getName() : null);
        model.addAttribute("appName", "TourPath");
        model.addAttribute("allCategories", Place.Category.values());
    }
}
