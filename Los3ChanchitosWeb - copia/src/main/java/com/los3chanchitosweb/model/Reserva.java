package com.los3chanchitosweb.model;

import java.time.LocalDateTime;

public class Reserva {
    private int idReserva;
    private int idUsuario; // ID del usuario que realiza la reserva
    private int idViaje;   // ID del viaje reservado
    private int cantidadAsientos;
    private LocalDateTime fechaReserva;
    private String estadoReserva; // Ej: "Confirmada", "Pendiente", "Cancelada"

    // Constructor vacío
    public Reserva() {
    }

    // Constructor completo
    public Reserva(int idReserva, int idUsuario, int idViaje, int cantidadAsientos, LocalDateTime fechaReserva, String estadoReserva) {
        this.idReserva = idReserva;
        this.idUsuario = idUsuario;
        this.idViaje = idViaje;
        this.cantidadAsientos = cantidadAsientos;
        this.fechaReserva = fechaReserva;
        this.estadoReserva = estadoReserva;
    }

    // Constructor para insertar (sin idReserva)
    public Reserva(int idUsuario, int idViaje, int cantidadAsientos, LocalDateTime fechaReserva, String estadoReserva) {
        this.idUsuario = idUsuario;
        this.idViaje = idViaje;
        this.cantidadAsientos = cantidadAsientos;
        this.fechaReserva = fechaReserva;
        this.estadoReserva = estadoReserva;
    }

    // Getters y Setters
    public int getIdReserva() {
        return idReserva;
    }

    public void setIdReserva(int idReserva) {
        this.idReserva = idReserva;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public int getIdViaje() {
        return idViaje;
    }

    public void setIdViaje(int idViaje) {
        this.idViaje = idViaje;
    }

    public int getCantidadAsientos() {
        return cantidadAsientos;
    }

    public void setCantidadAsientos(int cantidadAsientos) {
        this.cantidadAsientos = cantidadAsientos;
    }

    public LocalDateTime getFechaReserva() {
        return fechaReserva;
    }

    public void setFechaReserva(LocalDateTime fechaReserva) {
        this.fechaReserva = fechaReserva;
    }

    public String getEstadoReserva() {
        return estadoReserva;
    }

    public void setEstadoReserva(String estadoReserva) {
        this.estadoReserva = estadoReserva;
    }

    @Override
    public String toString() {
        return "Reserva{" +
                "idReserva=" + idReserva +
                ", idUsuario=" + idUsuario +
                ", idViaje=" + idViaje +
                ", cantidadAsientos=" + cantidadAsientos +
                ", fechaReserva=" + fechaReserva +
                ", estadoReserva='" + estadoReserva + '\'' +
                '}';
    }
}