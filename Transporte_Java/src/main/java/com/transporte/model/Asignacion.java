package com.transporte.model;


import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "asignaciones")
public class Asignacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "viaje_id")
    private Viaje viaje;

    @ManyToOne
    @JoinColumn(name = "chofer_id")
    private Chofer chofer;

    @ManyToOne
    @JoinColumn(name = "auxiliar_id")
    private Usuario auxiliar; // Usuario con rol AUXILIAR

    private LocalDateTime fechaAsignacion;
    private String observaciones;

    // Getters y setters
}