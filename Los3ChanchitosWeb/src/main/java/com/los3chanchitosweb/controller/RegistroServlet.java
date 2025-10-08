package com.los3chanchitosweb.controller;

import com.los3chanchitosweb.dao.UsuarioDAO;
import com.los3chanchitosweb.model.Usuario;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/registro") // Este es el mapping para el Servlet
public class RegistroServlet extends HttpServlet {

    private UsuarioDAO usuarioDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        this.usuarioDAO = new UsuarioDAO(); // Inicializa el DAO
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Simplemente redirige a la página de registro (registro.jsp) cuando se accede con GET
        request.getRequestDispatcher("/registro.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 1. Obtener los parámetros del formulario
        String nombre = request.getParameter("nombre");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String rol = request.getParameter("rol");

        // Validaciones básicas
        if (nombre == null || nombre.trim().isEmpty() ||
                email == null || email.trim().isEmpty() ||
                password == null || password.trim().isEmpty() ||
                rol == null || rol.trim().isEmpty()) {

            request.setAttribute("mensaje", "Todos los campos son obligatorios.");
            request.setAttribute("tipoMensaje", "error");
            request.getRequestDispatcher("/registro.jsp").forward(request, response);
            return;
        }

        // 2. Crear un objeto Usuario
        Usuario nuevoUsuario = new Usuario(nombre, email, password, rol);

        // 3. Intentar insertar el usuario usando el DAO
        try {
            // Primero, verificar si el email ya existe
            if (usuarioDAO.buscarUsuarioPorEmail(email) != null) {
                request.setAttribute("mensaje", "El email ya está registrado. Por favor, usa otro.");
                request.setAttribute("tipoMensaje", "error");
                request.getRequestDispatcher("/registro.jsp").forward(request, response);
                return;
            }

            if (usuarioDAO.insertarUsuario(nuevoUsuario)) {
                // Registro exitoso
                request.setAttribute("mensaje", "¡Registro exitoso! Ahora puedes iniciar sesión.");
                request.setAttribute("tipoMensaje", "success");
                // Redirige al login.jsp con un mensaje de éxito
                request.getRequestDispatcher("/login.jsp").forward(request, response);
            } else {
                // Fallo al insertar por alguna razón desconocida (ej. error de DB)
                request.setAttribute("mensaje", "Error al registrar el usuario. Intenta de nuevo.");
                request.setAttribute("tipoMensaje", "error");
                request.getRequestDispatcher("/registro.jsp").forward(request, response);
            }
        } catch (Exception e) {
            // Capturar cualquier otra excepción inesperada
            System.err.println("Excepción durante el registro: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("mensaje", "Ocurrió un error inesperado. Por favor, intenta más tarde.");
            request.setAttribute("tipoMensaje", "error");
            request.getRequestDispatcher("/registro.jsp").forward(request, response);
        }
    }
}