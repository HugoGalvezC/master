package com.los3chanchitosweb.controller;

import com.los3chanchitosweb.dao.AuxiliarDAO;
import com.los3chanchitosweb.dao.BusDAO;
import com.los3chanchitosweb.dao.ChoferDAO;
import com.los3chanchitosweb.dao.ViajeDAO;
import com.los3chanchitosweb.model.Auxiliar;
import com.los3chanchitosweb.model.Bus;
import com.los3chanchitosweb.model.Chofer;
import com.los3chanchitosweb.model.Viaje;
import com.los3chanchitosweb.util.DBConnection;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

@WebServlet("/planificarViaje")
public class PlanificacionServlet extends HttpServlet {

    private BusDAO busDAO;
    private ChoferDAO choferDAO;
    private AuxiliarDAO auxiliarDAO;
    private ViajeDAO viajeDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        busDAO = new BusDAO();
        choferDAO = new ChoferDAO();
        auxiliarDAO = new AuxiliarDAO();
        viajeDAO = new ViajeDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            List<Bus> buses = busDAO.obtenerTodosLosBuses();
            List<Chofer> choferes = choferDAO.obtenerTodosLosChoferes();
            List<Auxiliar> auxiliares = auxiliarDAO.obtenerTodosLosAuxiliares();

            request.setAttribute("buses", buses);
            request.setAttribute("choferes", choferes);
            request.setAttribute("auxiliares", auxiliares);

            request.getRequestDispatcher("/WEB-INF/views/planificacionViajes.jsp").forward(request, response);
        } catch (SQLException e) {
            System.err.println("Error SQL al cargar datos para planificar el viaje: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("mensaje", "Error en la base de datos al cargar opciones para planificar viaje.");
            request.setAttribute("tipoMensaje", "danger");
            request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
        } catch (Exception e) {
            System.err.println("Error inesperado al cargar datos para planificar el viaje: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("mensaje", "Ocurrió un error inesperado al cargar opciones para planificar viaje.");
            request.setAttribute("tipoMensaje", "danger");
            request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String origen = request.getParameter("origen");
        String destino = request.getParameter("destino");
        String fechaHoraSalidaStr = request.getParameter("fechaHoraSalida");
        String idBusStr = request.getParameter("idBus");
        String idChoferStr = request.getParameter("idChofer");
        String idAuxiliarStr = request.getParameter("idAuxiliar");
        String estadoViaje = request.getParameter("estadoViaje");

        String mensaje = "";
        String tipoMensaje = "";
        Connection conn = null;

        try {
            // Validaciones básicas
            if (origen == null || origen.isEmpty() || destino == null || destino.isEmpty() ||
                    fechaHoraSalidaStr == null || fechaHoraSalidaStr.isEmpty() ||
                    idBusStr == null || idBusStr.isEmpty() ||
                    idChoferStr == null || idChoferStr.isEmpty()) {
                throw new IllegalArgumentException("Todos los campos obligatorios (Origen, Destino, Fecha/Hora, Bus, Chofer) deben ser completados.");
            }

            // CAMBIO CLAVE AQUÍ: Ajustar el formato del DateTimeFormatter
            // input type="datetime-local" envía en formato yyyy-MM-ddTHH:mm
            LocalDateTime fechaHoraSalida = LocalDateTime.parse(fechaHoraSalidaStr, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"));

            int idBus = Integer.parseInt(idBusStr);
            int idChofer = Integer.parseInt(idChoferStr);
            Integer idAuxiliar = null;
            if (idAuxiliarStr != null && !idAuxiliarStr.isEmpty() && !idAuxiliarStr.equals("0")) {
                idAuxiliar = Integer.parseInt(idAuxiliarStr);
            }

            Bus busSeleccionado = busDAO.obtenerBusPorId(idBus);
            if (busSeleccionado == null) {
                throw new IllegalArgumentException("Bus seleccionado no válido o no encontrado.");
            }
            int capacidadAsientos = busSeleccionado.getCapacidadAsientos();

            if (estadoViaje == null || estadoViaje.isEmpty()) {
                estadoViaje = "Programado";
            }

            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            Viaje nuevoViaje = new Viaje(origen, destino, fechaHoraSalida, idBus, idChofer, idAuxiliar, capacidadAsientos, estadoViaje);

            boolean viajeInsertado = viajeDAO.insertarViaje(nuevoViaje);

            if (!viajeInsertado) {
                throw new SQLException("Fallo al insertar el nuevo viaje.");
            }

            Chofer choferAsignado = choferDAO.obtenerChoferPorId(idChofer);
            if (choferAsignado == null) {
                throw new IllegalArgumentException("Chofer seleccionado no válido o no encontrado.");
            }
            double horasViajeEstimadas = 8.0;
            choferAsignado.setHorasConduccionAcumuladas(choferAsignado.getHorasConduccionAcumuladas() + horasViajeEstimadas);

            boolean choferActualizado = choferDAO.actualizarChofer(choferAsignado);

            if (!choferActualizado) {
                throw new SQLException("Fallo al actualizar las horas de conducción del chofer.");
            }

            conn.commit();
            mensaje = "¡Viaje planificado con éxito y horas de chofer actualizadas!";
            tipoMensaje = "success";

        } catch (DateTimeParseException e) {
            // Mensaje ajustado para el formato real que envía datetime-local
            mensaje = "Error en el formato de fecha/hora. Asegúrese de que la fecha y hora sean válidas (yyyy-MM-ddTHH:mm).";
            tipoMensaje = "danger";
            System.err.println("DateTimeParseException: " + e.getMessage() + " - Input: " + fechaHoraSalidaStr);
            e.printStackTrace();
            if (conn != null) try { conn.rollback(); } catch (SQLException rb_e) { rb_e.printStackTrace(); }
        } catch (NumberFormatException e) {
            mensaje = "Error en el formato de ID de Bus, Chofer o Auxiliar.";
            tipoMensaje = "danger";
            e.printStackTrace();
            if (conn != null) try { conn.rollback(); } catch (SQLException rb_e) { rb_e.printStackTrace(); }
        } catch (IllegalArgumentException e) {
            mensaje = e.getMessage();
            tipoMensaje = "danger";
            e.printStackTrace();
            if (conn != null) try { conn.rollback(); } catch (SQLException rb_e) { rb_e.printStackTrace(); }
        } catch (SQLException e) {
            mensaje = "Error de base de datos al planificar el viaje: " + e.getMessage();
            tipoMensaje = "danger";
            e.printStackTrace();
            if (conn != null) try { conn.rollback(); } catch (SQLException rb_e) { rb_e.printStackTrace(); }
        } catch (Exception e) {
            mensaje = "Ocurrió un error inesperado al planificar el viaje: " + e.getMessage();
            tipoMensaje = "danger";
            e.printStackTrace();
            if (conn != null) try { conn.rollback(); } catch (SQLException rb_e) { rb_e.printStackTrace(); }
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    DBConnection.closeConnection(conn);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        request.setAttribute("mensaje", mensaje);
        request.setAttribute("tipoMensaje", tipoMensaje);
        doGet(request, response);
    }
}