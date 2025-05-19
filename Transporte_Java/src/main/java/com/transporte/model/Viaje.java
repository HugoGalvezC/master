package com.transporte.model;


import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Entity
@Table(name = "viajes")
public class Viaje {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ruta_id")
    private Ruta ruta;

    @ManyToOne
    @JoinColumn(name = "bus_id")
    private Bus bus;

    private LocalDate fecha;
    private LocalTime horaSalida;
    private int asientosDisponibles;

    @Enumerated(EnumType.STRING)
    private EstadoViaje estado;

    @OneToMany(mappedBy = "viaje")
    private List<Venta> ventas;

    // Getters, setters

    public enum EstadoViaje {
        PROGRAMADO, EN_RUTA, COMPLETADO, CANCELADO
    }
}