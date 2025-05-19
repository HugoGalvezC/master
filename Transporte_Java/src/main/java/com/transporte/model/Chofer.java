package com.transporte.model;

import jakarta.persistence.*;

@Entity
@Table(name = "choferes")
public class Chofer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    private String licencia;
    private int horasMensuales;
    private int horasAnuales;

    // Getters, setters
}