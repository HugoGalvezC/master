package com.los3chanchitosweb.model;

import java.time.LocalDateTime;
import java.util.List; // Para una lista de pasajes asociados

public class Venta {
    private int idVenta;
    private LocalDateTime fechaVenta;
    private int idVendedor; // FK al Usuario que realizó la venta
    private double montoTotal;
    private String estadoVenta;
    private List<Pasaje> pasajes; // Para guardar los detalles de los pasajes de esta venta

    // Constructor vacío
    public Venta() {}

    // Constructor para insertar (sin idVenta, con fecha actual)
    public Venta(int idVendedor, double montoTotal, String estadoVenta) {
        this.fechaVenta = LocalDateTime.now(); // Se establece al crear la venta
        this.idVendedor = idVendedor;
        this.montoTotal = montoTotal;
        this.estadoVenta = estadoVenta;
    }

    // Constructor completo para recuperar de la DB
    public Venta(int idVenta, LocalDateTime fechaVenta, int idVendedor, double montoTotal, String estadoVenta) {
        this.idVenta = idVenta;
        this.fechaVenta = fechaVenta;
        this.idVendedor = idVendedor;
        this.montoTotal = montoTotal;
        this.estadoVenta = estadoVenta;
    }

    // Getters y Setters
    public int getIdVenta() {
        return idVenta;
    }

    public void setIdVenta(int idVenta) {
        this.idVenta = idVenta;
    }

    public LocalDateTime getFechaVenta() {
        return fechaVenta;
    }

    public void setFechaVenta(LocalDateTime fechaVenta) {
        this.fechaVenta = fechaVenta;
    }

    public int getIdVendedor() {
        return idVendedor;
    }

    public void setIdVendedor(int idVendedor) {
        this.idVendedor = idVendedor;
    }

    public double getMontoTotal() {
        return montoTotal;
    }

    public void setMontoTotal(double montoTotal) {
        this.montoTotal = montoTotal;
    }

    public String getEstadoVenta() {
        return estadoVenta;
    }

    public void setEstadoVenta(String estadoVenta) {
        this.estadoVenta = estadoVenta;
    }

    public List<Pasaje> getPasajes() {
        return pasajes;
    }

    public void setPasajes(List<Pasaje> pasajes) {
        this.pasajes = pasajes;
    }

    @Override
    public String toString() {
        return "Venta{" +
                "idVenta=" + idVenta +
                ", fechaVenta=" + fechaVenta +
                ", idVendedor=" + idVendedor +
                ", montoTotal=" + montoTotal +
                ", estadoVenta='" + estadoVenta + '\'' +
                ", pasajes=" + pasajes +
                '}';
    }
}