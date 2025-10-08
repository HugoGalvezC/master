package com.los3chanchitosweb.model;

public class Bus {
    private int idBus;
    private String patente;
    private String modelo;
    private int capacidadAsientos; // Asegúrate de que el nombre de tu columna sea consistente (ej. 'capacidad_asientos' en DB)

    public Bus() {
        // Constructor por defecto
    }

    // Constructor para insertar (sin ID, si tu base de datos lo genera automáticamente)
    public Bus(String patente, String modelo, int capacidadAsientos) {
        this.patente = patente;
        this.modelo = modelo;
        this.capacidadAsientos = capacidadAsientos;
    }

    // Constructor para recuperar de la DB (con ID)
    public Bus(int idBus, String patente, String modelo, int capacidadAsientos) {
        this.idBus = idBus;
        this.patente = patente;
        this.modelo = modelo;
        this.capacidadAsientos = capacidadAsientos;
    }

    // --- Getters y Setters ---
    public int getIdBus() {
        return idBus;
    }

    public void setIdBus(int idBus) {
        this.idBus = idBus;
    }

    public String getPatente() {
        return patente;
    }

    public void setPatente(String patente) {
        this.patente = patente;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public int getCapacidadAsientos() {
        return capacidadAsientos;
    }

    public void setCapacidadAsientos(int capacidadAsientos) {
        this.capacidadAsientos = capacidadAsientos;
    }
}