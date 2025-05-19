package com.transporte.model;

import jakarta.persistence.*;

@Entity
@Table(name = "pasajeros")
public class Pasajero {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String apellido;

    @Column(unique = true)
    private String dni;

    private String telefono;

    @ManyToOne
    @JoinColumn(name = "venta_id")
    private Venta venta;

    private String asiento;
    private double precioPagado;

    // Getters, setters
}