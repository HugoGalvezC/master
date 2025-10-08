package com.los3chanchitosweb.controller;

import com.los3chanchitosweb.dao.ViajeDAO; // Asegúrate de que la ruta sea correcta
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/MarcarAsistenciaServlet") // Define la URL a la que responderá este Servlet
public class MarcarAsistenciaServlet extends HttpServlet {

    private static final long serialVersionUID = 1L; // Para serialización, es buena práctica
    private ViajeDAO viajeDAO; // Instancia del DAO

    // Constructor del Servlet para inicializar el DAO
    public MarcarAsistenciaServlet() {
        super();
        this.viajeDAO = new ViajeDAO();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Obtener el ID del viaje del formulario
        String idViajeParam = request.getParameter("idViaje");
        int idViaje = 0;
        String mensaje = "";
        String tipoMensaje = ""; // Para CSS (success, error)

        if (idViajeParam != null && !idViajeParam.isEmpty()) {
            try {
                idViaje = Integer.parseInt(idViajeParam);

                // Llamar al DAO para marcar la asistencia
                boolean exito = viajeDAO.marcarAsistenciaChofer(idViaje);

                if (exito) {
                    mensaje = "¡Asistencia marcada con éxito para el Viaje ID: " + idViaje + "!";
                    tipoMensaje = "success";
                } else {
                    mensaje = "No se pudo marcar la asistencia para el Viaje ID: " + idViaje + ". El viaje podría no existir o la asistencia ya está marcada.";
                    tipoMensaje = "warning"; // O error, si quieres un color más fuerte
                }
            } catch (NumberFormatException e) {
                mensaje = "Error: ID de viaje inválido.";
                tipoMensaje = "error";
                System.err.println("Error de formato al parsear idViaje: " + e.getMessage());
                e.printStackTrace();
            } catch (SQLException e) {
                mensaje = "Error de base de datos al marcar asistencia. Por favor, intente de nuevo.";
                tipoMensaje = "error";
                System.err.println("SQLException al marcar asistencia: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            mensaje = "Error: No se recibió el ID del viaje.";
            tipoMensaje = "error";
        }

        // Redirigir de vuelta al dashboard del chofer con un mensaje
        request.setAttribute("mensaje", mensaje);
        request.setAttribute("tipoMensaje", tipoMensaje);
        request.getRequestDispatcher("/WEB-INF/views/choferDashboard.jsp").forward(request, response);
    }

    // Si prefieres usar doGet para alguna razón (ej. pruebas directas), puedes añadirlo,
    // pero para formularios de acción, doPost es lo más común.
    // @Override
    // protected void doGet(HttpServletRequest request, HttpServletResponse response)
    //         throws ServletException, IOException {
    //     doPost(request, response); // O manejarlo diferente si es necesario
    // }
}