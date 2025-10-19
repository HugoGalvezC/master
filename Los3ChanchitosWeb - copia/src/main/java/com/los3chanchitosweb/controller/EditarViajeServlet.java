package com.los3chanchitosweb.controller;

import com.los3chanchitosweb.dao.BusDAO;
import com.los3chanchitosweb.dao.ChoferDAO;
import com.los3chanchitosweb.dao.AuxiliarDAO;
import com.los3chanchitosweb.dao.ViajeDAO;
import com.los3chanchitosweb.model.Viaje;
import com.los3chanchitosweb.model.Bus;
import com.los3chanchitosweb.model.Chofer;
import com.los3chanchitosweb.model.Auxiliar;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.sql.SQLException; // Importar SQLException

@WebServlet("/editarViaje")
public class EditarViajeServlet extends HttpServlet {

    private ViajeDAO viajeDAO;
    private BusDAO busDAO;
    private ChoferDAO choferDAO;
    private AuxiliarDAO auxiliarDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        viajeDAO = new ViajeDAO();
        busDAO = new BusDAO();
        choferDAO = new ChoferDAO();
        auxiliarDAO = new AuxiliarDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idViajeStr = request.getParameter("id");
        if (idViajeStr != null && !idViajeStr.isEmpty()) {
            try {
                int idViaje = Integer.parseInt(idViajeStr);
                Viaje viaje = viajeDAO.obtenerViajePorId(idViaje); // Ya lo corregimos en ViajeDAO para traer todos los campos

                if (viaje != null) {
                    request.setAttribute("viaje", viaje);

                    // Cargar listas para los select (Buses, Choferes, Auxiliares)
                    List<Bus> buses = busDAO.obtenerTodosLosBuses();
                    List<Chofer> choferes = choferDAO.obtenerTodosLosChoferes();
                    List<Auxiliar> auxiliares = auxiliarDAO.obtenerTodosLosAuxiliares();

                    request.setAttribute("buses", buses);
                    request.setAttribute("choferes", choferes);
                    request.setAttribute("auxiliares", auxiliares);

                    request.getRequestDispatcher("/WEB-INF/views/editarViaje.jsp").forward(request, response);
                } else {
                    request.getSession().setAttribute("mensaje", "Viaje no encontrado para editar.");
                    request.getSession().setAttribute("tipoMensaje", "danger");
                    response.sendRedirect(request.getContextPath() + "/listarViajes");
                }
            } catch (NumberFormatException e) {
                request.getSession().setAttribute("mensaje", "ID de viaje no válido para editar.");
                request.getSession().setAttribute("tipoMensaje", "danger");
                response.sendRedirect(request.getContextPath() + "/listarViajes");
            } catch (SQLException e) { // Capturar SQLException
                System.err.println("Error al cargar los datos del viaje para editar: " + e.getMessage());
                e.printStackTrace();
                request.getSession().setAttribute("mensaje", "Error al cargar los datos del viaje para editar.");
                request.getSession().setAttribute("tipoMensaje", "danger");
                response.sendRedirect(request.getContextPath() + "/listarViajes");
            }
        } else {
            request.getSession().setAttribute("mensaje", "No se proporcionó un ID de viaje para editar.");
            request.getSession().setAttribute("tipoMensaje", "danger");
            response.sendRedirect(request.getContextPath() + "/listarViajes");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Lógica para procesar la actualización del viaje
        String idViajeStr = request.getParameter("idViaje");
        String origen = request.getParameter("origen");
        String destino = request.getParameter("destino");
        String fechaHoraSalidaStr = request.getParameter("fechaHoraSalida");
        String idBusStr = request.getParameter("idBus");
        String idChoferStr = request.getParameter("idChofer");
        String idAuxiliarStr = request.getParameter("idAuxiliar"); // Puede ser "0" o vacío si es "Ninguno"
        String asientosDisponiblesStr = request.getParameter("asientosDisponibles");
        String estadoViaje = request.getParameter("estadoViaje");

        try {
            int idViaje = Integer.parseInt(idViajeStr);
            LocalDateTime fechaHoraSalida = LocalDateTime.parse(fechaHoraSalidaStr, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")); // Formato del input datetime-local
            int idBus = Integer.parseInt(idBusStr);
            int idChofer = Integer.parseInt(idChoferStr);
            Integer idAuxiliar = (idAuxiliarStr != null && !idAuxiliarStr.isEmpty() && !idAuxiliarStr.equals("0")) ? Integer.parseInt(idAuxiliarStr) : null;
            int asientosDisponibles = Integer.parseInt(asientosDisponiblesStr);

            // Recuperar los objetos completos de Bus, Chofer, Auxiliar para el objeto Viaje
            Bus bus = busDAO.obtenerBusPorId(idBus);
            Chofer chofer = choferDAO.obtenerChoferPorId(idChofer);
            Auxiliar auxiliar = null;
            if (idAuxiliar != null) {
                auxiliar = auxiliarDAO.obtenerAuxiliarPorId(idAuxiliar);
            }

            // ***** INICIO DE LA MODIFICACIÓN CLAVE PARA RESOLVER EL ERROR *****
            // 1. Obtener el viaje existente de la base de datos para recuperar sus campos de asistencia
            Viaje viajeExistente = viajeDAO.obtenerViajePorId(idViaje);
            if (viajeExistente == null) {
                request.getSession().setAttribute("mensaje", "Error: Viaje no encontrado para actualizar.");
                request.getSession().setAttribute("tipoMensaje", "danger");
                response.sendRedirect(request.getContextPath() + "/listarViajes");
                return; // Importante salir del método si el viaje no se encuentra
            }

            // 2. Crear el objeto Viaje actualizado, pasando TODOS los parámetros al constructor,
            // incluyendo los de asistencia que se obtuvieron del viaje existente.
            Viaje viajeActualizado = new Viaje(
                    idViaje,
                    origen,
                    destino,
                    fechaHoraSalida,
                    asientosDisponibles,
                    bus,
                    chofer,
                    auxiliar,
                    estadoViaje,
                    viajeExistente.getEstadoAsistenciaChofer(),   // Tomar del viaje existente
                    viajeExistente.getFechaHoraAsistenciaChofer() // Tomar del viaje existente
            );
            // ***** FIN DE LA MODIFICACIÓN CLAVE *****

            boolean actualizado = viajeDAO.actualizarViaje(viajeActualizado);

            if (actualizado) {
                request.getSession().setAttribute("mensaje", "Viaje actualizado con éxito.");
                request.getSession().setAttribute("tipoMensaje", "success");
            } else {
                request.getSession().setAttribute("mensaje", "No se pudo actualizar el viaje.");
                request.getSession().setAttribute("tipoMensaje", "danger");
            }
        } catch (NumberFormatException | DateTimeParseException e) {
            System.err.println("Error en el formato de los datos: " + e.getMessage());
            e.printStackTrace();
            request.getSession().setAttribute("mensaje", "Error en el formato de los datos: Por favor, verifique.");
            request.getSession().setAttribute("tipoMensaje", "danger");
            // Redirige a la página de listar viajes o a una de error para informar al usuario
        } catch (SQLException e) { // Capturar SQLException de las operaciones de DAO
            System.err.println("Error de base de datos al actualizar viaje: " + e.getMessage());
            e.printStackTrace();
            request.getSession().setAttribute("mensaje", "Error al actualizar el viaje en la base de datos.");
            request.getSession().setAttribute("tipoMensaje", "danger");
            // Redirige a la página de listar viajes o a una de error para informar al usuario
        } catch (Exception e) { // Capturar cualquier otra excepción inesperada
            System.err.println("Error inesperado al actualizar viaje: " + e.getMessage());
            e.printStackTrace();
            request.getSession().setAttribute("mensaje", "Error inesperado al actualizar el viaje.");
            request.getSession().setAttribute("tipoMensaje", "danger");
        }
        response.sendRedirect(request.getContextPath() + "/listarViajes");
    }
}