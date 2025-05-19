package com.transporte.model;


import jakarta.persistence.*;

@Entity
@Table(name = "asientos")
public class Asiento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "bus_id")
    private Bus bus;

    private String numero;
    private String tipo; // Ejecutivo, Premium, Económico

    @Enumerated(EnumType.STRING)
    private EstadoAsiento estado;

    // Getters y setters

    public enum EstadoAsiento {
        DISPONIBLE, OCUPADO, RESERVADO, INHABILITADO
    }
}