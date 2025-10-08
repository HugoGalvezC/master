package com.los3chanchitosweb.model;

public class Pasajero {
    private int idPasajero;
    private String nombre;
    private String apellido;
    private String dni;
    private String email;
    private String telefono;

    // Constructor vacío
    public Pasajero() {}

    // Constructor con todos los campos (excluyendo idPasajero para inserciones)
    public Pasajero(String nombre, String apellido, String dni, String email, String telefono) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
        this.email = email;
        this.telefono = telefono;
    }

    // Constructor con todos los campos (incluyendo idPasajero para recuperación)
    public Pasajero(int idPasajero, String nombre, String apellido, String dni, String email, String telefono) {
        this.idPasajero = idPasajero;
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
        this.email = email;
        this.telefono = telefono;
    }

    // Getters y Setters
    public int getIdPasajero() {
        return idPasajero;
    }

    public void setIdPasajero(int idPasajero) {
        this.idPasajero = idPasajero;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    @Override
    public String toString() {
        return "Pasajero{" +
                "idPasajero=" + idPasajero +
                ", nombre='" + nombre + '\'' +
                ", apellido='" + apellido + '\'' +
                ", dni='" + dni + '\'' +
                ", email='" + email + '\'' +
                ", telefono='" + telefono + '\'' +
                '}';
    }
}