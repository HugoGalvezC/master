package com.transporte.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
public class ReporteDTO {
    private String tipoReporte;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private int totalVentas;
    private BigDecimal ingresosTotales;
    private BigDecimal gastosTotales;
    private BigDecimal utilidadNeta;
    private List<DetalleReporteDTO> detalles;

    @Data
    public static class DetalleReporteDTO {
        private String ruta;
        private int cantidadPasajeros;
        private BigDecimal ingresos;
        private double porcentajeOcupacion;
    }
}