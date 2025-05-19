package com.transporte.repository;


import com.transporte.model.Asiento;
import com.transporte.model.Asiento.EstadoAsiento;
import com.transporte.model.Bus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
@Repository
public interface AsientoRepository extends JpaRepository<Asiento, Long> {
    List<Asiento> findByBusAndEstado(Bus bus, EstadoAsiento estado);
    List<Asiento> findByBusAndTipo(Bus bus, String tipo);
}