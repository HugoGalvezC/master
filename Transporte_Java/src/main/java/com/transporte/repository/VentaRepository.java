package com.transporte.repository;

import com.transporte.model.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository

public interface VentaRepository extends JpaRepository<Venta, Long> {
    List<Venta> findByFechaVentaBetween(LocalDateTime inicio, LocalDateTime fin);

    @Query("SELECT v FROM Venta v WHERE v.vendedor.id = :vendedorId")
    List<Venta> findByVendedor(Long vendedorId);
}