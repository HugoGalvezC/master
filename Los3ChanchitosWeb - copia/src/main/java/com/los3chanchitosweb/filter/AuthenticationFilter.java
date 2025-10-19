package com.los3chanchitosweb.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter(urlPatterns = {"/usuarios/*"}) // Aplica este filtro a todas las URLs bajo /usuarios
public class AuthenticationFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Se ejecuta al inicializar el filtro
        System.out.println("AuthenticationFilter inicializado.");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String path = req.getRequestURI();
        String contextPath = req.getContextPath();

        System.out.println("Filtrando: " + path); // Para depuración

        // Excluir URLs públicas que no requieren autenticación
        // Asegúrate de que login y registro (y sus JSPs) sean accesibles sin login
        if (path.startsWith(contextPath + "/login") ||
                path.startsWith(contextPath + "/registro") ||
                path.equals(contextPath + "/login.jsp") || // Si acceden directamente al JSP de login
                path.equals(contextPath + "/registro.jsp") || // Si acceden directamente al JSP de registro
                path.equals(contextPath + "/") || // Página de inicio (si no requiere login)
                path.contains("/css/") || // Excluir archivos CSS
                path.contains("/js/") ||  // Excluir archivos JavaScript
                path.contains("/images/")) { // Excluir imágenes
            chain.doFilter(request, response); // Deja pasar la solicitud
            return;
        }

        // Verificar si hay una sesión y si el atributo "loggedInUser" existe
        HttpSession session = req.getSession(false); // 'false' para no crear una nueva sesión si no existe

        if (session == null || session.getAttribute("loggedInUser") == null) {
            System.out.println("Usuario no autenticado. Redirigiendo a login.");
            // Si no hay sesión o no hay usuario logueado, redirigir a la página de login
            // *** CAMBIO AQUÍ: Añadir el parámetro 'reason' ***
            res.sendRedirect(contextPath + "/login?reason=auth_required");
            return; // Detener el procesamiento de la cadena de filtros y del recurso solicitado
        } else {
            // El usuario está autenticado, permite que la solicitud continúe a su destino original
            System.out.println("Usuario autenticado. Continuando.");
            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {
        // Se ejecuta al destruir el filtro (ej. al apagar el servidor)
        System.out.println("AuthenticationFilter destruido.");
    }
}