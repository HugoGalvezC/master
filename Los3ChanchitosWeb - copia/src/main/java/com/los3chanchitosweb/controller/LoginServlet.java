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

@WebServlet("/login") // Mapea este Servlet a la URL /login
public class LoginServlet extends HttpServlet {

    private UsuarioDAO usuarioDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        usuarioDAO = new UsuarioDAO();
    }

    // Maneja solicitudes GET (para mostrar el formulario de login)
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // *** CAMBIO AQUÍ: Añadir lógica para el mensaje de redirección ***
        String redirectReason = request.getParameter("reason");
        if ("auth_required".equals(redirectReason)) {
            request.setAttribute("loginMessage", "Necesitas iniciar sesión para acceder a esa página.");
        }
        // Simplemente redirige al JSP del formulario de login
        request.getRequestDispatcher("/WEB-INF/login.jsp").forward(request, response);
    }

    // Maneja solicitudes POST (para procesar el envío del formulario de login)
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String loginMessage = null;

        System.out.println("email:"+email + " password: "+ password);


        if (email == null || email.isEmpty() || password == null || password.isEmpty()) {
            loginMessage = "Por favor, ingresa tu email y contraseña.";
        } else {

            System.out.println("no es vacio....");

            // Llama al DAO para verificar las credenciales
            boolean loginExitoso = usuarioDAO.verificarContrasena(email, password);

            System.out.println("loginExitoso " + loginExitoso);

            if (loginExitoso) {
                Usuario loggedInUser = usuarioDAO.buscarUsuarioPorEmail(email);
                if (loggedInUser != null) {
                    // Login exitoso: Establecer una sesión de usuario
                    HttpSession session = request.getSession();
                    session.setAttribute("loggedInUser", loggedInUser);
                    session.setMaxInactiveInterval(30 * 60); // Sesión expira en 30 minutos

                    // Redirigir a una página de éxito (ej. dashboard, lista de usuarios)
                    response.sendRedirect(request.getContextPath() + "/usuarios"); // Redirige al Servlet de usuarios
                    return; // Importante para detener la ejecución del doPost aquí
                } else {
                    loginMessage = "Error interno: Usuario autenticado no encontrado.";
                }
            } else {
                loginMessage = "Credenciales incorrectas. Intenta de nuevo.";
                request.setAttribute("email", email); // Para que el email persista en el campo si hay error
            }
        }

        // Si hay un error o no se redirigió, se vuelve a mostrar el formulario de login con el mensaje
        request.setAttribute("loginMessage", loginMessage);
        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }

    // También podemos agregar un doDelete y doPut si es necesario
}