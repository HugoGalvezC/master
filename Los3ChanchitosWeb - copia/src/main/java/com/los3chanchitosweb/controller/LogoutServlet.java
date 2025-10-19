package com.los3chanchitosweb.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false); // No crear una nueva sesión si no existe
        if (session != null) {
            session.invalidate(); // Invalida la sesión actual
        }
        // Redirige al usuario a la página de login (o a la página principal)
        response.sendRedirect(request.getContextPath() + "/login");
    }

    // Opcional: Si prefieres que el logout sea una petición POST por seguridad, puedes implementarlo así.
    // Aunque para cerrar sesión, GET es comúnmente aceptado si solo invalida la sesión.
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response); // Simplemente llama al doGet para manejar el logout
    }
}