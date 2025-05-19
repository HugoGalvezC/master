package com.transporte.repository;

import com.transporte.model.Bus;
import com.transporte.model.Bus.EstadoBus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository

public interface BusRepository extends JpaRepository<Bus, Long> {
    List<Bus> findByEstado(EstadoBus estado);
    Optional<Bus> findByPlaca(String placa);
}