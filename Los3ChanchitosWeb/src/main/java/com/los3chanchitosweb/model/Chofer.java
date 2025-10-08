package com.los3chanchitosweb.model;

public class Chofer {
    private int idChofer;
    private String nombre;
    // Si tu tabla de base de datos tiene una columna 'apellido' para choferes, descomenta la línea de abajo y ajusta los constructores y getters/setters.
    // private String apellido;
    private String licencia;
    private double horasConduccionAcumuladas; // Asegúrate de que el nombre de tu columna sea consistente

    public Chofer() {
        // Constructor por defecto
    }

    // Constructor para insertar (sin ID)
    public Chofer(String nombre, String licencia, double horasConduccionAcumuladas) {
        this.nombre = nombre;
        // this.apellido = apellido; // Descomentar si usas apellido
        this.licencia = licencia;
        this.horasConduccionAcumuladas = horasConduccionAcumuladas;
    }

    // Constructor para recuperar de la DB (con ID)
    public Chofer(int idChofer, String nombre, String licencia, double horasConduccionAcumuladas) {
        this.idChofer = idChofer;
        this.nombre = nombre;
        // this.apellido = apellido; // Descomentar si usas apellido
        this.licencia = licencia;
        this.horasConduccionAcumuladas = horasConduccionAcumuladas;
    }

    // Si tu DAO recupera apellido, necesitarás un constructor con él también, ej:
    // public Chofer(int idChofer, String nombre, String apellido, String licencia, double horasConduccionAcumuladas) {
    //     this.idChofer = idChofer;
    //     this.nombre = nombre;
    //     this.apellido = apellido;
    //     this.licencia = licencia;
    //     this.horasConduccionAcumuladas = horasConduccionAcumuladas;
    // }

    // --- Getters y Setters ---
    public int getIdChofer() {
        return idChofer;
    }

    public void setIdChofer(int idChofer) {
        this.idChofer = idChofer;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    // public String getApellido() { return apellido; } // Descomentar si usas apellido
    // public void setApellido(String apellido) { this.apellido = apellido; } // Descomentar si usas apellido

    public String getLicencia() {
        return licencia;
    }

    public void setLicencia(String licencia) {
        this.licencia = licencia;
    }

    public double getHorasConduccionAcumuladas() {
        return horasConduccionAcumuladas;
    }

    public void setHorasConduccionAcumuladas(double horasConduccionAcumuladas) {
        this.horasConduccionAcumuladas = horasConduccionAcumuladas;
    }
}