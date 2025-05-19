package com.transporte.service;

import com.transporte.dto.ReporteDTO;
import java.time.LocalDate;

public interface ReporteService {
    ReporteDTO generarReporteVentasDiarias(LocalDate fecha);
    ReporteDTO generarReporteUtilidades(LocalDate inicio, LocalDate fin);
    ReporteDTO generarReporteOcupacion(LocalDate inicio, LocalDate fin);
}
