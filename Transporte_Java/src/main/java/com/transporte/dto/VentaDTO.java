package com.transporte.dto;
import java.util.List;


public class VentaDTO {
    private Long viajeId;
    private String metodoPago;
    private double total;
    private List<PasajeroDTO> pasajeros;

    // Getters y Setters

    public static class PasajeroDTO {
        private String nombre;
        private String apellido;
        private String dni;
        private String telefono;
        private String asiento;
        private double precio;

        // Getters y Setters
    }
}
