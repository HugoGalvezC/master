package com.transporte.model;


import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "ventas")
public class Venta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "viaje_id")
    private Viaje viaje;

    @ManyToOne
    @JoinColumn(name = "vendedor_id")
    private Usuario vendedor;

    private LocalDateTime fechaVenta;
    private double total;

    @Enumerated(EnumType.STRING)
    private MetodoPago metodoPago;

    @OneToMany(mappedBy = "venta", cascade = CascadeType.ALL)
    private List<Pasajero> pasajeros;

    // Getters, setters

    public enum MetodoPago {
        EFECTIVO, TARJETA, TRANSFERENCIA
    }
}