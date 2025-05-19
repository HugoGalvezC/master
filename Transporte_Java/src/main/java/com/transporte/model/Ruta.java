package com.transporte.model;


import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "rutas")
public class Ruta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String origen;
    private String destino;
    private double distanciaKm;
    private int duracionEstimadaMin;
    private double precioBase;

    @OneToMany(mappedBy = "ruta")
    private List<Viaje> viajes = new ArrayList<>();

    // Getters, setters
}