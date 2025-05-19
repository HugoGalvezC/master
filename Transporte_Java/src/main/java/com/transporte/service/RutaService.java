package com.transporte.service;

import com.transporte.model.Ruta;
import java.util.List;

public interface RutaService {
    Ruta crearRuta(Ruta ruta);
    Ruta actualizarRuta(Ruta ruta);
    List<Ruta> buscarRutasPorOrigenDestino(String origen, String destino);
    List<Ruta> listarTodasRutas();
}