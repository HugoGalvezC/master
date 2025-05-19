package com.transporte.repository;

import com.transporte.model.Pasajero;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository

public interface PasajeroRepository extends JpaRepository<Pasajero, Long> {
    Optional<Pasajero> findByDni(String dni);
    List<Pasajero> findByVentaViajeId(Long viajeId);
}