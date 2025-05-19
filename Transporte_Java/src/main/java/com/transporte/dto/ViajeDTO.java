package com.transporte.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public class ViajeDTO {
    private Long id;
    private Long rutaId;
    private Long busId;
    private LocalDate fecha;
    private LocalTime horaSalida;
    private int asientosDisponibles;
    private String estado;

    // Getters y Setters
}