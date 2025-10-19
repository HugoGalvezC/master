package com.los3chanchitosweb.model;

public class Auxiliar {
    private int idAuxiliar;
    private String nombre;
    // Si tu tabla de base de datos tiene una columna 'apellido' para auxiliares, descomenta la línea de abajo y ajusta los constructores y getters/setters.
    // private String apellido;

    public Auxiliar() {
        // Constructor por defecto
    }

    // Constructor para insertar (sin ID)
    public Auxiliar(String nombre) {
        this.nombre = nombre;
        // this.apellido = apellido; // Descomentar si usas apellido
    }

    // Constructor para recuperar de la DB (con ID)
    public Auxiliar(int idAuxiliar, String nombre) {
        this.idAuxiliar = idAuxiliar;
        this.nombre = nombre;
        // this.apellido = apellido; // Descomentar si usas apellido
    }

    // Si tu DAO recupera apellido, necesitarás un constructor con él también, ej:
    // public Auxiliar(int idAuxiliar, String nombre, String apellido) { ... }

    // --- Getters y Setters ---
    public int getIdAuxiliar() {
        return idAuxiliar;
    }

    public void setIdAuxiliar(int idAuxiliar) {
        this.idAuxiliar = idAuxiliar;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    // public String getApellido() { return apellido; } // Descomentar si usas apellido
    // public void setApellido(String apellido) { this.apellido = apellido; } // Descomentar si usas apellido
}