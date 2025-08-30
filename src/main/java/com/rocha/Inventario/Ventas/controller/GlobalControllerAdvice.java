package com.rocha.Inventario.Ventas.controller;

import java.security.Principal;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalControllerAdvice {
    @ModelAttribute("username")
    public String addUsernameToModel(Principal principal) {
        return principal != null ? principal.getName() : "Invitado";
    }
}
