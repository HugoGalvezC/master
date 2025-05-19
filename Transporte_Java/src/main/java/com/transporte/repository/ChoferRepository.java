package com.transporte.repository;

import com.transporte.model.Chofer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository


public interface ChoferRepository extends JpaRepository<Chofer, Long> {
    @Query("SELECT c FROM Chofer c WHERE c.horasMensuales < :limiteHoras")
    List<Chofer> findChoferesDisponibles(int limiteHoras);
}