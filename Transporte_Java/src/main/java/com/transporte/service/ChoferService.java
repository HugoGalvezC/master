package com.transporte.service;

import com.transporte.model.Chofer;
import java.util.List;

public interface ChoferService {
    Chofer registrarChofer(Chofer chofer);
    Chofer actualizarChofer(Chofer chofer);
    List<Chofer> listarTodosChoferes();
    Chofer obtenerChoferPorId(Long id);
    void eliminarChofer(Long id);

    Object obtenerAsignaciones(Long id);
}