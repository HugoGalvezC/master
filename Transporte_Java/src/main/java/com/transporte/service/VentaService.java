package com.transporte.service;

import com.transporte.dto.VentaDTO;
import com.transporte.model.Venta;
import java.time.LocalDate;
import java.util.List;

public interface VentaService {
    Venta registrarVenta(VentaDTO ventaDTO);
    void anularVenta(Long ventaId);
    List<Venta> listarVentasPorFecha(LocalDate fecha);
    List<Venta> listarVentasPorVendedor(Long vendedorId);
}