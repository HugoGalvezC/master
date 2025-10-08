package com.los3chanchitosweb.controller;

import com.los3chanchitosweb.dao.UsuarioDAO;
import com.los3chanchitosweb.model.Usuario;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession; // Importar HttpSession
import java.io.IOException;
import java.util.List;

@WebServlet("/usuarios")
public class UsuarioServlet extends HttpServlet {

    private UsuarioDAO usuarioDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        usuarioDAO = new UsuarioDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Lógica de protección:
        HttpSession session = request.getSession(false); // No crear sesión si no existe

        // Si el usuario no está logueado, redirigir al login
        if (session == null || session.getAttribute("loggedInUser") == null) {
            response.sendRedirect(request.getContextPath() + "/login?reason=auth_required"); // Agregamos un parámetro para el mensaje
            return; // Detener la ejecución
        }

        // Si el usuario está logueado, continuar con la lógica
        try {
            List<Usuario> usuarios = usuarioDAO.obtenerTodosLosUsuarios();
            request.setAttribute("listaUsuarios", usuarios);
            // CAMBIO CLAVE AQUÍ: Apunta a la ubicación correcta del JSP dentro de WEB-INF/views
            request.getRequestDispatcher("/WEB-INF/views/listaUsuarios.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            // Puedes manejar el error de una manera más amigable para el usuario
            request.setAttribute("mensajeError", "Error al cargar la lista de usuarios: " + e.getMessage());
            // Asegúrate de tener un archivo error.jsp en /WEB-INF/views/ o en la raíz
            request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
        }
    }
}