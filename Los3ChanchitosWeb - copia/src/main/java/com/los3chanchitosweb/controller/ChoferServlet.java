package com.los3chanchitosweb.controller;

import com.los3chanchitosweb.dao.ViajeDAO;
import com.los3chanchitosweb.model.Usuario;
import com.los3chanchitosweb.model.Viaje;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/choferDashboard")
public class ChoferServlet extends HttpServlet {

    private ViajeDAO viajeDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        viajeDAO = new ViajeDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        Usuario loggedInUser = null;
        if (session != null) {
            loggedInUser = (Usuario) session.getAttribute("loggedInUser");
        }

        // 1. Verificar autenticación y rol
        if (session == null || loggedInUser == null || !"chofer".equalsIgnoreCase(loggedInUser.getRol())) {
            response.sendRedirect(request.getContextPath() + "/login?reason=auth_required");
            return;
        }

        try {
            // 2. Obtener el próximo viaje asignado al chofer
            // Suponemos que tienes un método en ViajeDAO para esto
            // Necesitarás implementar este método en ViajeDAO
            Viaje proximoViaje = viajeDAO.obtenerProximoViajeAsignadoAChofer(loggedInUser.getId()); // loggedInUser.getId() debería ser el id_chofer

            request.setAttribute("viajeAsignado", proximoViaje);
            request.getRequestDispatcher("/WEB-INF/views/choferDashboard.jsp").forward(request, response);

        } catch (SQLException e) {
            System.err.println("ERROR: SQLException al cargar dashboard del chofer: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("mensaje", "Error al cargar la información de tu dashboard.");
            request.setAttribute("tipoMensaje", "error");
            request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
        }
    }

    // No hay doPost directo aquí, las acciones como "Marcar Asistencia"
    // serán manejadas por servlets dedicados (como MarcarAsistenciaServlet)
    // a los que se redirige desde el JSP.
}