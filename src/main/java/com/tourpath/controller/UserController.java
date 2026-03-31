package com.tourpath.controller;

import com.tourpath.dto.UserProfileDTO;
import com.tourpath.model.User;
import com.tourpath.repository.UserRepository;
import com.tourpath.service.ReviewService;
import com.tourpath.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.time.LocalDateTime;

@Controller
@RequestMapping("/user")
@PreAuthorize("isAuthenticated()")
public class UserController {

    @Autowired private UserService     userService;
    @Autowired private UserRepository  userRepository;
    @Autowired private ReviewService   reviewService;
    @Autowired private PasswordEncoder passwordEncoder;

    @GetMapping("/profile")
    public String profile(Authentication auth, Model model) {
        User user = userService.getUserByUsername(auth.getName())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        UserProfileDTO dto = new UserProfileDTO();
        dto.setFullName(user.getFullName());
        dto.setEmail(user.getEmail());
        dto.setPhone(user.getPhone());

        model.addAttribute("user", user);
        model.addAttribute("profileDTO", dto);
        model.addAttribute("myReviews", reviewService.getReviewsByUser(user.getId()));
        return "user/profile";
    }

    @PostMapping("/profile")
    public String updateProfile(@Valid @ModelAttribute("profileDTO") UserProfileDTO dto,
                                BindingResult result,
                                Authentication auth,
                                RedirectAttributes ra,
                                Model model) {
        User user = userService.getUserByUsername(auth.getName())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (result.hasErrors()) {
            model.addAttribute("user", user);
            model.addAttribute("myReviews", reviewService.getReviewsByUser(user.getId()));
            return "user/profile";
        }

        // Cambio de contraseña opcional
        if (dto.getNewPassword() != null && !dto.getNewPassword().isBlank()) {
            if (dto.getCurrentPassword() == null ||
                    !passwordEncoder.matches(dto.getCurrentPassword(), user.getPassword())) {
                model.addAttribute("user", user);
                model.addAttribute("myReviews", reviewService.getReviewsByUser(user.getId()));
                model.addAttribute("pwdError", "La contraseña actual es incorrecta.");
                return "user/profile";
            }
            if (!dto.getNewPassword().equals(dto.getConfirmNewPassword())) {
                model.addAttribute("user", user);
                model.addAttribute("myReviews", reviewService.getReviewsByUser(user.getId()));
                model.addAttribute("pwdError", "Las contraseñas nuevas no coinciden.");
                return "user/profile";
            }
            user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        }

        user.setFullName(dto.getFullName());
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone());
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);

        ra.addFlashAttribute("success", "✅ Perfil actualizado correctamente.");
        return "redirect:/user/profile";
    }

    @PostMapping("/reviews/{id}/delete")
    public String deleteReview(@PathVariable String id, RedirectAttributes ra) {
        reviewService.deleteReview(id);
        ra.addFlashAttribute("success", "Reseña eliminada.");
        return "redirect:/user/profile";
    }
}
