package com.transporte.controller;

import com.transporte.dto.VentaDTO;
import com.transporte.model.Usuario;
import com.transporte.service.VentaService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/ventas")
public class VentaController {

    private final VentaService ventaService;

    public VentaController(VentaService ventaService) {
        this.ventaService = ventaService;
    }

    @GetMapping("/nueva")
    public String nuevaVenta(Model model) {
        // Lógica para mostrar formulario de venta
        return "ventas/nueva";
    }

    @PostMapping("/guardar")
    public String guardarVenta(@ModelAttribute VentaDTO ventaDto,
                               @AuthenticationPrincipal Usuario usuario) {
        // Lógica para guardar venta
        return "redirect:/ventas/exito";
    }
}