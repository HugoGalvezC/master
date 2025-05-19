package com.transporte.service;

import com.transporte.model.Asignacion;
import com.transporte.model.Chofer;
import com.transporte.model.Viaje;
import java.util.List;



public interface AsignacionService {
    Asignacion asignarChoferAViaje(Chofer chofer, Viaje viaje);
    void reasignarChofer(Long asignacionId, Chofer nuevoChofer);
    List<Asignacion> listarAsignacionesPorChofer(Long choferId);
    List<Asignacion> listarAsignacionesPorViaje(Long viajeId);
}