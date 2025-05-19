package com.transporte.controller;

import com.transporte.dto.VentaDto;
import com.transporte.model.Usuario;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/ventas")
public class VentaController {

    private final VentaService ventaService;
    private final ViajeService viajeService;

    public VentaController(VentaService ventaService, ViajeService viajeService) {
        this.ventaService = ventaService;
        this.viajeService = viajeService;
    }

    @GetMapping("/nueva")
    public String mostrarFormularioVenta(Model model) {
        model.addAttribute("venta", new VentaDto());
        model.addAttribute("viajes", viajeService.obtenerViajesDisponibles());
        return "ventas/nueva";
    }

    @PostMapping("/guardar")
    public String procesarVenta(@ModelAttribute VentaDto ventaDto,
                                @AuthenticationPrincipal Usuario usuario,
                                Model model) {
        try {
            Venta venta = ventaService.registrarVenta(ventaDto, usuario);
            return "redirect:/ventas/comprobante/" + venta.getId();
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("venta", ventaDto);
            model.addAttribute("viajes", viajeService.obtenerViajesDisponibles());
            return "ventas/nueva";
        }
    }

    @GetMapping("/comprobante/{id}")
    public String mostrarComprobante(@PathVariable Long id, Model model) {
        model.addAttribute("venta", ventaService.obtenerVentaConDetalles(id));
        return "ventas/comprobante";
    }
}