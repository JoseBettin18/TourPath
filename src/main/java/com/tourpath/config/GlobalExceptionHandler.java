package com.tourpath.config;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public String handleAccessDenied(Model model) {
        model.addAttribute("errorCode", "403");
        model.addAttribute("errorTitle", "Acceso denegado");
        model.addAttribute("errorMessage", "No tienes permisos para acceder a esta sección.");
        return "error/error";
    }

    @ExceptionHandler(NoResourceFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleNoResource(Model model) {
        model.addAttribute("errorCode", "404");
        model.addAttribute("errorTitle", "Página no encontrada");
        model.addAttribute("errorMessage", "La página que buscas no existe.");
        return "error/error";
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleRuntime(RuntimeException ex, Model model) {
        model.addAttribute("errorCode", "500");
        model.addAttribute("errorTitle", "Error inesperado");
        model.addAttribute("errorMessage", ex.getMessage() != null ? ex.getMessage() : "Intenta de nuevo.");
        return "error/error";
    }
}
