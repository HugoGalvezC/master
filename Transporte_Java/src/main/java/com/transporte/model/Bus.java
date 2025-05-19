package com.transporte.model;


import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "buses")
public class Bus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String placa;

    private String modelo;
    private int capacidad;
    private int anio;

    @Enumerated(EnumType.STRING)
    private EstadoBus estado;

    // Getters, setters

    public enum EstadoBus {
        DISPONIBLE, MANTENIMIENTO, INACTIVO
    }
}