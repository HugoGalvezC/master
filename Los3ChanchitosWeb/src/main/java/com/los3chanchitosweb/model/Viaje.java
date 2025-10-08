package com.los3chanchitosweb.model;

import java.time.LocalDateTime;
import java.time.ZoneId; // Importar ZoneId para la conversión
import java.util.Date; // Importar Date para la conversión

public class Viaje {
    private int idViaje;
    private String origen;
    private String destino;
    private LocalDateTime fechaHoraSalida;
    private int asientosDisponibles;
    private Bus bus;
    private Chofer chofer;
    private Auxiliar auxiliar;
    private String estadoViaje;

    // -- START ADDED SECTION --
    private String estadoAsistenciaChofer; // Nuevo campo para el estado de asistencia
    private LocalDateTime fechaHoraAsistenciaChofer; // Nuevo campo para la fecha y hora de la asistencia
    // -- END ADDED SECTION --

    public Viaje() {
        // Constructor por defecto
    }

    // Constructor para insertar un nuevo viaje (recibe IDs de los objetos relacionados)
    // NOTA: Este constructor no maneja estadoAsistenciaChofer y fechaHoraAsistenciaChofer porque se asume que
    // al insertar un viaje, estos valores son por defecto ('Pendiente', null) en la DB.
    public Viaje(String origen, String destino, LocalDateTime fechaHoraSalida, int idBus, int idChofer, Integer idAuxiliar, int asientosDisponibles, String estadoViaje) {
        this.origen = origen;
        this.destino = destino;
        this.fechaHoraSalida = fechaHoraSalida;
        this.bus = new Bus();
        this.bus.setIdBus(idBus);
        this.chofer = new Chofer();
        this.chofer.setIdChofer(idChofer);
        if (idAuxiliar != null) {
            this.auxiliar = new Auxiliar();
            this.auxiliar.setIdAuxiliar(idAuxiliar);
        } else {
            this.auxiliar = null;
        }
        this.asientosDisponibles = asientosDisponibles;
        this.estadoViaje = estadoViaje;
        // Los nuevos campos se inicializarán con sus valores por defecto (null para String y LocalDateTime)
        // o según los valores DEFAULT definidos en la base de datos ('Pendiente' para estado_asistencia_chofer)
    }

    // Constructor para recuperar un Viaje de la DB (MODIFICADO para incluir los nuevos campos)
    public Viaje(int idViaje, String origen, String destino, LocalDateTime fechaHoraSalida,
                 int asientosDisponibles, Bus bus, Chofer chofer, Auxiliar auxiliar, String estadoViaje
                 // -- START MODIFIED SECTION --
            , String estadoAsistenciaChofer, LocalDateTime fechaHoraAsistenciaChofer
                 // -- END MODIFIED SECTION --
    ) {
        this.idViaje = idViaje;
        this.origen = origen;
        this.destino = destino;
        this.fechaHoraSalida = fechaHoraSalida;
        this.asientosDisponibles = asientosDisponibles;
        this.bus = bus;
        this.chofer = chofer;
        this.auxiliar = auxiliar;
        this.estadoViaje = estadoViaje;
        // -- START MODIFIED SECTION --
        this.estadoAsistenciaChofer = estadoAsistenciaChofer;
        this.fechaHoraAsistenciaChofer = fechaHoraAsistenciaChofer;
        // -- END MODIFIED SECTION --
    }

    // --- Getters y Setters ---
    public int getIdViaje() {
        return idViaje;
    }

    public void setIdViaje(int idViaje) {
        this.idViaje = idViaje;
    }

    public String getOrigen() {
        return origen;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public LocalDateTime getFechaHoraSalida() {
        return fechaHoraSalida;
    }

    public void setFechaHoraSalida(LocalDateTime fechaHoraSalida) {
        this.fechaHoraSalida = fechaHoraSalida;
    }

    // **NUEVO MÉTODO:** Para usar con <fmt:formatDate> en JSP
    public Date getFechaHoraSalidaAsDate() {
        if (this.fechaHoraSalida == null) {
            return null;
        }
        // Convierte LocalDateTime a Instant, y luego a Date
        return Date.from(this.fechaHoraSalida.atZone(ZoneId.systemDefault()).toInstant());
    }

    public int getAsientosDisponibles() {
        return asientosDisponibles;
    }

    public void setAsientosDisponibles(int asientosDisponibles) {
        this.asientosDisponibles = asientosDisponibles;
    }

    public Bus getBus() {
        return bus;
    }

    public void setBus(Bus bus) {
        this.bus = bus;
    }

    public Chofer getChofer() {
        return chofer;
    }

    public void setChofer(Chofer chofer) {
        this.chofer = chofer;
    }

    public Auxiliar getAuxiliar() {
        return auxiliar;
    }

    public void setAuxiliar(Auxiliar auxiliar) {
        this.auxiliar = auxiliar;
    }

    public String getEstadoViaje() {
        return estadoViaje;
    }

    public void setEstadoViaje(String estadoViaje) {
        this.estadoViaje = estadoViaje;
    }

    public int getIdBus() {
        return bus != null ? bus.getIdBus() : 0;
    }

    public int getIdChofer() {
        return chofer != null ? chofer.getIdChofer() : 0;
    }

    public Integer getIdAuxiliar() {
        return auxiliar != null ? auxiliar.getIdAuxiliar() : null;
    }

    // -- START ADDED SECTION - Getters and Setters for new attendance fields --
    public String getEstadoAsistenciaChofer() {
        return estadoAsistenciaChofer;
    }

    public void setEstadoAsistenciaChofer(String estadoAsistenciaChofer) {
        this.estadoAsistenciaChofer = estadoAsistenciaChofer;
    }

    public LocalDateTime getFechaHoraAsistenciaChofer() {
        return fechaHoraAsistenciaChofer;
    }

    public void setFechaHoraAsistenciaChofer(LocalDateTime fechaHoraAsistenciaChofer) {
        this.fechaHoraAsistenciaChofer = fechaHoraAsistenciaChofer;
    }
    // -- END ADDED SECTION --
}