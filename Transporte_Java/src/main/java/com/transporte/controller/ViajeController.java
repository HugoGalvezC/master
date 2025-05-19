package com.transporte.controller;


import com.transporte.service.ViajeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/viajes")
public class ViajeController {

    private final ViajeService viajeService;

    public ViajeController(ViajeService viajeService) {
        this.viajeService = viajeService;
    }

    @GetMapping
    public String listarViajes(Model model) {
        model.addAttribute("viajes", viajeService.obtenerTodosViajes());
        return "viajes/lista";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevoViaje(Model model) {
        // Lógica para formulario
        return "viajes/formulario";
    }
}