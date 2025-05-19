package com.transporte.service;
import com.transporte.model.Viaje;
import java.time.LocalDate;
import java.util.List;

public interface ViajeService {
    Viaje crearViaje(Viaje viaje);
    Viaje actualizarViaje(Viaje viaje);
    void cancelarViaje(Long id);
    List<Viaje> buscarViajesPorFecha(LocalDate fecha);
    List<Viaje> buscarViajesPorRuta(Long rutaId);
    List<Viaje> buscarViajesDisponibles();
}