package com.transporte.repository;

import com.transporte.model.Asignacion;
import com.transporte.model.Chofer;
import com.transporte.model.Viaje;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
@Repository

public interface AsignacionRepository extends JpaRepository<Asignacion, Long> {

    /**
     * Encuentra asignaciones por chofer
     */
    List<Asignacion> findByChofer(Chofer chofer);

    /**
     * Encuentra asignaciones por viaje
     */
    List<Asignacion> findByViaje(Viaje viaje);

    /**
     * Encuentra asignaciones activas para un chofer en un rango de fechas
     */
    @Query("SELECT a FROM Asignacion a WHERE a.chofer = :chofer AND a.viaje.fecha BETWEEN :fechaInicio AND :fechaFin")
    List<Asignacion> findAsignacionesPorChoferYFecha(
            @Param("chofer") Chofer chofer,
            @Param("fechaInicio") LocalDateTime fechaInicio,
            @Param("fechaFin") LocalDateTime fechaFin);

    /**
     * Verifica si un chofer ya está asignado a otro viaje en el mismo horario
     */
    @Query("SELECT COUNT(a) > 0 FROM Asignacion a WHERE a.chofer = :chofer " +
            "AND a.viaje.fecha = :fecha " +
            "AND (a.viaje.horaSalida BETWEEN :horaInicio AND :horaFin " +
            "OR :horaInicio BETWEEN a.viaje.horaSalida AND FUNCTION('ADDTIME', a.viaje.horaSalida, CONCAT(a.viaje.ruta.duracionEstimadaMin, ' MINUTE')))")
    boolean existsChoferAsignadoEnMismoHorario(
            @Param("chofer") Chofer chofer,
            @Param("fecha") LocalDate fecha,
            @Param("horaInicio") LocalTime horaInicio,
            @Param("horaFin") LocalTime horaFin);

    /**
     * Obtiene el historial de asignaciones recientes
     */
    @Query("SELECT a FROM Asignacion a ORDER BY a.fechaAsignacion DESC LIMIT 10")
    List<Asignacion> findUltimasAsignaciones();
}