package com.transporte.repository;

import com.transporte.model.Viaje;
import com.transporte.model.Viaje.EstadoViaje;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;


@Repository

public interface ViajeRepository extends JpaRepository<Viaje, Long> {
    List<Viaje> findByFechaAndEstado(LocalDate fecha, EstadoViaje estado);

    @Query("SELECT v FROM Viaje v WHERE v.ruta.id = :rutaId AND v.fecha BETWEEN :fechaInicio AND :fechaFin")
    List<Viaje> findByRutaAndFechaBetween(Long rutaId, LocalDate fechaInicio, LocalDate fechaFin);

    @Query("SELECT v FROM Viaje v WHERE v.asientosDisponibles > 0 AND v.estado = 'PROGRAMADO'")
    List<Viaje> findViajesDisponibles();
}