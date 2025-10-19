package com.los3chanchitosweb.controller;

import com.los3chanchitosweb.dao.ViajeDAO;
import com.los3chanchitosweb.model.Viaje;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession; // Importar HttpSession
import java.io.IOException;
import java.util.List;

@WebServlet("/listarViajes")
public class ListarViajesServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private ViajeDAO viajeDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        viajeDAO = new ViajeDAO(); // Inicializa tu ViajeDAO
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Lógica de protección (similar a UsuarioServlet, ya que ambas son páginas protegidas)
        HttpSession session = request.getSession(false); // No crear sesión si no existe

        if (session == null || session.getAttribute("loggedInUser") == null) {
            response.sendRedirect(request.getContextPath() + "/login?reason=auth_required"); // Redirige al login si no hay sesión
            return; // Detener la ejecución
        }

        try {
            List<Viaje> listaViajes = viajeDAO.obtenerTodosLosViajes();
            request.setAttribute("viajes", listaViajes);
            // Esta ruta ya estaba bien, apunta a la ubicación correcta del JSP
            request.getRequestDispatcher("/WEB-INF/views/listaViajes.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            // Puedes manejar el error de una manera más amigable para el usuario
            request.setAttribute("mensajeError", "Error al cargar la lista de viajes: " + e.getMessage());
            // Asegúrate de tener un archivo error.jsp en /WEB-INF/views/ o en la raíz
            request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
        }
    }
}