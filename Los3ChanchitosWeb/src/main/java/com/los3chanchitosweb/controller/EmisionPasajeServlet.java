package com.los3chanchitosweb.controller;

import com.los3chanchitosweb.dao.PasajeDAO;
import com.los3chanchitosweb.dao.ViajeDAO;
import com.los3chanchitosweb.dao.PasajeroDAO;
import com.los3chanchitosweb.dao.BusDAO;
import com.los3chanchitosweb.dao.ChoferDAO;
import com.los3chanchitosweb.dao.AuxiliarDAO;
import com.los3chanchitosweb.model.Pasaje;
import com.los3chanchitosweb.model.Viaje;
import com.los3chanchitosweb.model.Pasajero;
import com.los3chanchitosweb.model.Bus;
import com.los3chanchitosweb.model.Chofer;
import com.los3chanchitosweb.model.Auxiliar;
import com.los3chanchitosweb.model.Usuario; // Para la seguridad

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList; // Para manejar múltiples pasajes si se emite una venta completa
import java.util.List;

@WebServlet("/emitirPasaje")
public class EmisionPasajeServlet extends HttpServlet {

    private PasajeDAO pasajeDAO;
    private ViajeDAO viajeDAO;
    private PasajeroDAO pasajeroDAO;
    private BusDAO busDAO;
    private ChoferDAO choferDAO;
    private AuxiliarDAO auxiliarDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        pasajeDAO = new PasajeDAO();
        viajeDAO = new ViajeDAO();
        pasajeroDAO = new PasajeroDAO();
        busDAO = new BusDAO();
        choferDAO = new ChoferDAO();
        auxiliarDAO = new AuxiliarDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // --- Lógica de Seguridad: Verificar la sesión y el usuario autenticado ---
        HttpSession session = request.getSession(false);
        Usuario loggedInUser = null;
        if (session != null) {
            loggedInUser = (Usuario) session.getAttribute("loggedInUser");
        }

        // Si no hay sesión o no hay usuario logueado, redirige al login
        if (session == null || loggedInUser == null) {
            System.out.println("DEBUG: Usuario no autenticado para /emitirPasaje. Redirigiendo a login.");
            response.sendRedirect(request.getContextPath() + "/login?reason=auth_required");
            return;
        }

        // --- Restricción de Rol: VENDEDOR, CHOFER y JEFE DE OPERACIONES pueden ver pasajes ---
        String userRole = loggedInUser.getRol();
        if (!"vendedor".equalsIgnoreCase(userRole) && !"chofer".equalsIgnoreCase(userRole) && !"jefe_operaciones".equalsIgnoreCase(userRole)) {
            request.setAttribute("mensaje", "Acceso denegado. Solo roles autorizados pueden ver pasajes.");
            request.setAttribute("tipoMensaje", "error");
            System.out.println("DEBUG: Intento de acceso a /emitirPasaje por usuario con rol " + userRole);
            request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
            return;
        }
        // --- Fin Lógica de Seguridad y Rol ---

        String idVentaParam = request.getParameter("idVenta");
        String idPasajeParam = request.getParameter("idPasaje"); // Opcional, si queremos emitir un solo pasaje

        List<Pasaje> pasajesEmitidos = new ArrayList<>();
        String mensaje = "";
        String tipoMensaje = "error";

        try {
            if (idVentaParam != null && !idVentaParam.isEmpty()) {
                int idVenta = Integer.parseInt(idVentaParam);
                // Obtener todos los pasajes asociados a esta venta
                List<Pasaje> pasajesDeVenta = pasajeDAO.obtenerPasajesPorIdVenta(idVenta);

                if (pasajesDeVenta.isEmpty()) {
                    mensaje = "No se encontraron pasajes para la venta con ID: " + idVenta;
                    tipoMensaje = "warning";
                } else {
                    for (Pasaje pasaje : pasajesDeVenta) {
                        // Para cada pasaje, obtener la información detallada
                        Viaje viaje = viajeDAO.obtenerViajePorId(pasaje.getIdViaje());
                        Pasajero pasajero = pasajeroDAO.obtenerPasajeroPorId(pasaje.getIdPasajero());

                        // Asegúrate de que viaje, pasajero, bus, chofer, auxiliar no son nulos
                        // y setea la información completa en el objeto Pasaje o un DTO
                        if (viaje != null) {
                            pasaje.setViaje(viaje); // Asume que Pasaje.java tiene un setViaje(Viaje viaje)
                            pasaje.setBus(busDAO.obtenerBusPorId(viaje.getIdBus())); // Asume Pasaje.java tiene setBus(Bus bus)
                            pasaje.setChofer(choferDAO.obtenerChoferPorId(viaje.getIdChofer())); // Asume Pasaje.java tiene setChofer(Chofer chofer)
                            if (viaje.getIdAuxiliar() != 0) { // Si hay auxiliar asignado
                                pasaje.setAuxiliar(auxiliarDAO.obtenerAuxiliarPorId(viaje.getIdAuxiliar())); // Asume Pasaje.java tiene setAuxiliar(Auxiliar auxiliar)
                            }
                        }
                        if (pasajero != null) {
                            pasaje.setPasajero(pasajero); // Asume Pasaje.java tiene setPasajero(Pasajero pasajero)
                        }
                        pasajesEmitidos.add(pasaje);
                    }
                    mensaje = "Pasaje(s) de la venta " + idVenta + " listo(s) para emitir.";
                    tipoMensaje = "success";
                }
            } else if (idPasajeParam != null && !idPasajeParam.isEmpty()) {
                // Lógica para emitir un solo pasaje si se pasa idPasaje
                int idPasaje = Integer.parseInt(idPasajeParam);
                Pasaje pasaje = pasajeDAO.obtenerPasajePorId(idPasaje);

                if (pasaje == null) {
                    mensaje = "No se encontró el pasaje con ID: " + idPasaje;
                    tipoMensaje = "warning";
                } else {
                    Viaje viaje = viajeDAO.obtenerViajePorId(pasaje.getIdViaje());
                    Pasajero pasajero = pasajeroDAO.obtenerPasajeroPorId(pasaje.getIdPasajero());

                    if (viaje != null) {
                        pasaje.setViaje(viaje);
                        pasaje.setBus(busDAO.obtenerBusPorId(viaje.getIdBus()));
                        pasaje.setChofer(choferDAO.obtenerChoferPorId(viaje.getIdChofer()));
                        if (viaje.getIdAuxiliar() != 0) {
                            pasaje.setAuxiliar(auxiliarDAO.obtenerAuxiliarPorId(viaje.getIdAuxiliar()));
                        }
                    }
                    if (pasajero != null) {
                        pasaje.setPasajero(pasajero);
                    }
                    pasajesEmitidos.add(pasaje); // Añadir el único pasaje a la lista
                    mensaje = "Pasaje ID " + idPasaje + " listo para emitir.";
                    tipoMensaje = "success";
                }

            } else {
                mensaje = "No se proporcionó un ID de venta ni un ID de pasaje.";
                tipoMensaje = "warning";
            }

            request.setAttribute("pasajesEmitidos", pasajesEmitidos);
            request.setAttribute("mensaje", mensaje);
            request.setAttribute("tipoMensaje", tipoMensaje);
            request.getRequestDispatcher("/WEB-INF/views/emitirPasaje.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            mensaje = "ID de venta o pasaje inválido.";
            System.err.println("ERROR: NumberFormatException en EmisionPasajeServlet: " + e.getMessage());
            request.setAttribute("mensaje", mensaje);
            request.setAttribute("tipoMensaje", "error");
            request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
        } catch (SQLException e) {
            mensaje = "Error en la base de datos al obtener información del pasaje: " + e.getMessage();
            System.err.println("ERROR: SQLException en EmisionPasajeServlet: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("mensaje", mensaje);
            request.setAttribute("tipoMensaje", "error");
            request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
        } catch (Exception e) {
            mensaje = "Ocurrió un error inesperado: " + e.getMessage();
            System.err.println("ERROR: Excepción general en EmisionPasajeServlet: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("mensaje", mensaje);
            request.setAttribute("tipoMensaje", "error");
            request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
        }
    }

    // No necesitamos un doPost para la emisión, ya que es solo para mostrar.
}