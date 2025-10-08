package com.los3chanchitosweb.controller;

import com.los3chanchitosweb.dao.ReservaDAO;
import com.los3chanchitosweb.dao.ViajeDAO;
import com.los3chanchitosweb.model.Reserva;
import com.los3chanchitosweb.model.Viaje;
import com.los3chanchitosweb.model.Usuario; // Necesitas la clase Usuario para obtener el ID
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.time.LocalDateTime;

@WebServlet("/reservarViaje") // Asegúrate de que esta URL sea la que usa tu botón
public class ReservarViajeServlet extends HttpServlet {

    private ViajeDAO viajeDAO;
    private ReservaDAO reservaDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        viajeDAO = new ViajeDAO();
        reservaDAO = new ReservaDAO();
    }

    // El botón "Reservar" en listarViajes.jsp usa un enlace <a href="...">,
    // lo cual dispara un GET. Por lo tanto, doGet es el método principal para esto.
    // doPost se usaría si tuvieras un formulario en reservaViaje.jsp que envía datos.
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // En este escenario, si el POST es para confirmar la reserva,
        // podrías procesar los datos del formulario aquí.
        // Por ahora, como el doGet está haciendo la reserva directa,
        // simplemente redirigimos para no duplicar lógica, o podrías eliminar doPost si no lo necesitas.
        doGet(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // --- Lógica de Seguridad: Verificar la sesión y el usuario autenticado ---
        // Usar getSession(false) para NO crear una nueva sesión si no existe.
        // Usar "loggedInUser" para obtener el usuario, siendo consistente con LoginServlet y AuthenticationFilter.
        HttpSession session = request.getSession(false); // <--- CAMBIO CLAVE AQUÍ
        Usuario usuario = null;
        if (session != null) {
            usuario = (Usuario) session.getAttribute("loggedInUser"); // <--- CAMBIO CLAVE AQUÍ
        }

        // Si no hay sesión o no hay usuario logueado, redirige al login
        if (session == null || usuario == null) {
            System.out.println("DEBUG: Usuario no autenticado para /reservarViaje. Redirigiendo a login.");
            response.sendRedirect(request.getContextPath() + "/login?reason=auth_required");
            return;
        }
        // --- Fin Lógica de Seguridad ---

        // 1. Obtener el ID del viaje desde la solicitud (del parámetro 'id' en la URL)
        String idViajeParam = request.getParameter("id");
        if (idViajeParam == null || idViajeParam.isEmpty()) {
            request.setAttribute("mensaje", "ID de viaje no especificado para reservar.");
            request.setAttribute("tipoMensaje", "error");
            System.out.println("DEBUG: ID de viaje no especificado.");
            // Redirige de nuevo a listarViajes, pasando los mensajes como parámetros de URL
            response.sendRedirect(request.getContextPath() + "/listarViajes?mensaje=" + request.getAttribute("mensaje") + "&tipoMensaje=" + request.getAttribute("tipoMensaje"));
            return; // Termina la ejecución
        }

        int idViaje;
        try {
            idViaje = Integer.parseInt(idViajeParam);
        } catch (NumberFormatException e) {
            request.setAttribute("mensaje", "ID de viaje inválido para reservar.");
            request.setAttribute("tipoMensaje", "error");
            System.out.println("DEBUG: ID de viaje inválido: " + idViajeParam);
            response.sendRedirect(request.getContextPath() + "/listarViajes?mensaje=" + request.getAttribute("mensaje") + "&tipoMensaje=" + request.getAttribute("tipoMensaje"));
            return; // Termina la ejecución
        }

        // 2. Obtener el viaje de la base de datos
        Viaje viaje = null;
        try {
            viaje = viajeDAO.obtenerViajePorId(idViaje); // Asegúrate de que este método existe y funciona
        } catch (Exception e) {
            System.err.println("ERROR: Excepción al obtener viaje por ID en ReservarViajeServlet: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("mensaje", "Error interno al obtener los detalles del viaje.");
            request.setAttribute("tipoMensaje", "error");
            response.sendRedirect(request.getContextPath() + "/listarViajes?mensaje=" + request.getAttribute("mensaje") + "&tipoMensaje=" + request.getAttribute("tipoMensaje"));
            return; // Termina la ejecución
        }

        if (viaje == null) {
            request.setAttribute("mensaje", "Viaje no encontrado para reservar.");
            request.setAttribute("tipoMensaje", "error");
            System.out.println("DEBUG: Viaje con ID " + idViaje + " no encontrado.");
            response.sendRedirect(request.getContextPath() + "/listarViajes?mensaje=" + request.getAttribute("mensaje") + "&tipoMensaje=" + request.getAttribute("tipoMensaje"));
            return; // Termina la ejecución
        }

        // --- Lógica de Reserva ---
        // Por ahora, asumimos que se reserva 1 asiento por defecto al hacer clic en "Reservar".
        // En una aplicación real, aquí redirigirías a un JSP para que el usuario elija la cantidad de asientos.
        int cantidadAsientosAReservar = 1;

        // 3. Verificar disponibilidad de asientos
        if (viaje.getAsientosDisponibles() < cantidadAsientosAReservar) {
            request.setAttribute("mensaje", "No hay suficientes asientos disponibles en este viaje para reservar.");
            request.setAttribute("tipoMensaje", "error");
            System.out.println("DEBUG: Asientos insuficientes para viaje ID " + idViaje);
            response.sendRedirect(request.getContextPath() + "/listarViajes?mensaje=" + request.getAttribute("mensaje") + "&tipoMensaje=" + request.getAttribute("tipoMensaje"));
            return; // Termina la ejecución
        }

        // 4. Crear la reserva
        Reserva nuevaReserva = new Reserva(
                usuario.getId(), // ID del usuario logueado (Asegúrate que Usuario.getId() devuelve el int/long correcto)
                idViaje,
                cantidadAsientosAReservar,
                LocalDateTime.now(), // Fecha y hora actual de la reserva
                "Confirmada" // Estado inicial de la reserva
        );

        boolean reservaExitosa = false;
        try {
            reservaExitosa = reservaDAO.insertarReserva(nuevaReserva); // Asegúrate de que este método existe y funciona
            System.out.println("DEBUG: Intento de insertar reserva: " + reservaExitosa);

            if (reservaExitosa) {
                // 5. Actualizar los asientos disponibles en el viaje
                viaje.setAsientosDisponibles(viaje.getAsientosDisponibles() - cantidadAsientosAReservar);
                boolean actualizacionViajeExitosa = viajeDAO.actualizarViaje(viaje); // Asegúrate de que este método existe y funciona
                System.out.println("DEBUG: Intento de actualizar viaje: " + actualizacionViajeExitosa);

                if (actualizacionViajeExitosa) {
                    request.setAttribute("mensaje", "¡Reserva realizada con éxito!");
                    request.setAttribute("tipoMensaje", "success");
                    System.out.println("DEBUG: Reserva exitosa para viaje ID " + idViaje);
                } else {
                    // Considera añadir lógica para deshacer la reserva (rollback) si la actualización del viaje falla.
                    request.setAttribute("mensaje", "Error al actualizar los asientos del viaje. La reserva puede estar incompleta.");
                    request.setAttribute("tipoMensaje", "error");
                    System.out.println("DEBUG: Fallo al actualizar asientos para viaje ID " + idViaje);
                }
            } else {
                request.setAttribute("mensaje", "Error al registrar la reserva en la base de datos.");
                request.setAttribute("tipoMensaje", "error");
                System.out.println("DEBUG: Fallo al registrar la reserva en la DB.");
            }
        } catch (Exception e) {
            System.err.println("ERROR: Excepción durante el proceso de reserva: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("mensaje", "Ocurrió un error inesperado al procesar la reserva.");
            request.setAttribute("tipoMensaje", "error");
        }

        // 6. Redirigir de vuelta a la lista de viajes con el mensaje
        // URLEncoder.encode es importante si los mensajes contienen espacios u caracteres especiales
        String encodedMensaje = java.net.URLEncoder.encode((String) request.getAttribute("mensaje"), "UTF-8");
        String encodedTipoMensaje = java.net.URLEncoder.encode((String) request.getAttribute("tipoMensaje"), "UTF-8");
        response.sendRedirect(request.getContextPath() + "/listarViajes?mensaje=" + encodedMensaje + "&tipoMensaje=" + encodedTipoMensaje);
    }
}