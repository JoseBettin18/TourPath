package com.tourpath.controller;

import com.tourpath.config.JwtUtil;
import com.tourpath.dto.RegisterDTO;
import com.tourpath.service.CustomUserDetailsService;
import com.tourpath.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private CustomUserDetailsService userDetailsService;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String loginPage(@RequestParam(required = false) String error,
            @RequestParam(required = false) String logout,
            Model model) {
        if (error != null)
            model.addAttribute("error", "Credenciales incorrectas.");
        if (logout != null)
            model.addAttribute("message", "Sesión cerrada correctamente.");
        return "auth/login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username,
            @RequestParam String password,
            HttpServletResponse response,
            RedirectAttributes ra) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password));
            UserDetails ud = userDetailsService.loadUserByUsername(username);
            String token = jwtUtil.generateToken(ud);
            Cookie cookie = new Cookie("tp_token", token);
            cookie.setHttpOnly(true);
            cookie.setMaxAge(86400);
            cookie.setPath("/");
            response.addCookie(cookie);

            // Redirigir según rol
            boolean isSuperAdmin = ud.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_SUPER_ADMIN"));
            boolean isOwner = ud.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_OWNER"));

            if (isSuperAdmin)
                return "redirect:/superadmin/dashboard";
            if (isOwner)
                return "redirect:/owner/dashboard";
            return "redirect:/home";
        } catch (BadCredentialsException e) {
            ra.addFlashAttribute("error", "Credenciales incorrectas.");
            return "redirect:/auth/login?error=true";
        }
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("registerDTO", new RegisterDTO());
        return "auth/register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("registerDTO") RegisterDTO dto,
            BindingResult result,
            RedirectAttributes ra,
            Model model) {
        if (result.hasErrors())
            return "auth/register";
        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            model.addAttribute("error", "Las contraseñas no coinciden.");
            return "auth/register";
        }
        try {
            userService.registerUser(dto);
            ra.addFlashAttribute("success", "¡Cuenta creada! Inicia sesión.");
            return "redirect:/auth/login";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "auth/register";
        }
    }
}
