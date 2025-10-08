package com.los3chanchitosweb.model;

public class Pasaje {
    private int idPasaje;
    private int idVenta; // FK a Venta
    private int idViaje; // FK a Viaje
    private int idPasajero; // FK a Pasajero
    private double precio;
    private Integer numeroAsiento; // Usamos Integer para que pueda ser null si no se asigna
    private String estadoPasaje;

    // --- NUEVAS PROPIEDADES PARA LA EMISIÓN DE PASAJES ---
    private Viaje viaje;
    private Pasajero pasajero;
    private Bus bus;
    private Chofer chofer;
    private Auxiliar auxiliar;
    // ---------------------------------------------------

    // Constructor vacío
    public Pasaje() {}

    // Constructor para insertar (sin idPasaje)
    public Pasaje(int idVenta, int idViaje, int idPasajero, double precio, Integer numeroAsiento, String estadoPasaje) {
        this.idVenta = idVenta;
        this.idViaje = idViaje;
        this.idPasajero = idPasajero;
        this.precio = precio;
        this.numeroAsiento = numeroAsiento;
        this.estadoPasaje = estadoPasaje;
    }

    // Constructor completo para recuperar de la DB
    public Pasaje(int idPasaje, int idVenta, int idViaje, int idPasajero, double precio, Integer numeroAsiento, String estadoPasaje) {
        this.idPasaje = idPasaje;
        this.idVenta = idVenta;
        this.idViaje = idViaje;
        this.idPasajero = idPasajero;
        this.precio = precio;
        this.numeroAsiento = numeroAsiento;
        this.estadoPasaje = estadoPasaje;
    }

    // Getters y Setters
    public int getIdPasaje() {
        return idPasaje;
    }

    public void setIdPasaje(int idPasaje) {
        this.idPasaje = idPasaje;
    }

    public int getIdVenta() {
        return idVenta;
    }

    public void setIdVenta(int idVenta) {
        this.idVenta = idVenta;
    }

    public int getIdViaje() {
        return idViaje;
    }

    public void setIdViaje(int idViaje) {
        this.idViaje = idViaje;
    }

    public int getIdPasajero() {
        return idPasajero;
    }

    public void setIdPasajero(int idPasajero) {
        this.idPasajero = idPasajero;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public Integer getNumeroAsiento() {
        return numeroAsiento;
    }

    public void setNumeroAsiento(Integer numeroAsiento) {
        this.numeroAsiento = numeroAsiento;
    }

    public String getEstadoPasaje() {
        return estadoPasaje;
    }

    public void setEstadoPasaje(String estadoPasaje) {
        this.estadoPasaje = estadoPasaje;
    }

    @Override
    public String toString() {
        return "Pasaje{" +
                "idPasaje=" + idPasaje +
                ", idVenta=" + idVenta +
                ", idViaje=" + idViaje +
                ", idPasajero=" + idPasajero +
                ", precio=" + precio +
                ", numeroAsiento=" + numeroAsiento +
                ", estadoPasaje='" + estadoPasaje + '\'' +
                '}';
    }

    // --- NUEVOS GETTERS Y SETTERS PARA LAS PROPIEDADES RELACIONADAS ---
    public Viaje getViaje() {
        return viaje;
    }

    public void setViaje(Viaje viaje) {
        this.viaje = viaje;
    }

    public Pasajero getPasajero() {
        return pasajero;
    }

    public void setPasajero(Pasajero pasajero) {
        this.pasajero = pasajero;
    }

    public Bus getBus() {
        return bus;
    }

    public void setBus(Bus bus) {
        this.bus = bus;
    }

    public Chofer getChofer() {
        return chofer;
    }

    public void setChofer(Chofer chofer) {
        this.chofer = chofer;
    }

    public Auxiliar getAuxiliar() {
        return auxiliar;
    }

    public void setAuxiliar(Auxiliar auxiliar) {
        this.auxiliar = auxiliar;
    }
    // ------------------------------------------------------------------
}