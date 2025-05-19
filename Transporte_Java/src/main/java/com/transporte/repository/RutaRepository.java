package com.transporte.repository;

import com.transporte.model.Ruta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository

public interface RutaRepository extends JpaRepository<Ruta, Long> {
    @Query("SELECT r FROM Ruta r WHERE r.origen = :origen AND r.destino = :destino")
    List<Ruta> findByOrigenAndDestino(String origen, String destino);
}