package com.transporte.controller;

import com.transporte.model.Usuario;
import com.transporte.service.ChoferService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/chofer")
public class ChoferController {

    private final ChoferService choferService;

    public ChoferController(ChoferService choferService) {
        this.choferService = choferService;
    }

    @GetMapping("/asignaciones")
    public String verAsignaciones(@AuthenticationPrincipal Usuario usuario, Model model) {
        // Verificación de seguridad adicional
        if (usuario == null || usuario.getRol() != Usuario.Rol.CHOFER) {
            return "redirect:/acceso-denegado";
        }

        model.addAttribute("asignaciones", choferService.obtenerAsignaciones(usuario.getId()));
        return "chofer/asignaciones";
    }
}