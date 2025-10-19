package com.los3chanchitosweb.controller;

import com.los3chanchitosweb.dao.ViajeDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/eliminarViaje")
public class EliminarViajeServlet extends HttpServlet {

    private ViajeDAO viajeDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        viajeDAO = new ViajeDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idViajeStr = request.getParameter("id"); // Obtiene el ID del parámetro URL

        if (idViajeStr != null && !idViajeStr.isEmpty()) {
            try {
                int idViaje = Integer.parseInt(idViajeStr);
                boolean eliminado = viajeDAO.eliminarViaje(idViaje); // Llama al método DAO
                if (eliminado) {
                    request.getSession().setAttribute("mensaje", "Viaje eliminado con éxito.");
                    request.getSession().setAttribute("tipoMensaje", "success");
                } else {
                    request.getSession().setAttribute("mensaje", "No se pudo eliminar el viaje o no existe.");
                    request.getSession().setAttribute("tipoMensaje", "danger");
                }
            } catch (NumberFormatException e) {
                request.getSession().setAttribute("mensaje", "ID de viaje no válido.");
                request.getSession().setAttribute("tipoMensaje", "danger");
                e.printStackTrace();
            } catch (Exception e) {
                request.getSession().setAttribute("mensaje", "Error al intentar eliminar el viaje: " + e.getMessage());
                request.getSession().setAttribute("tipoMensaje", "danger");
                e.printStackTrace();
            }
        } else {
            request.getSession().setAttribute("mensaje", "No se proporcionó un ID de viaje para eliminar.");
            request.getSession().setAttribute("tipoMensaje", "danger");
        }

        // Redirige de nuevo a la lista de viajes después de la operación
        response.sendRedirect(request.getContextPath() + "/listarViajes");
    }

    // No se necesita doPost para esta funcionalidad simple, ya que se maneja con GET
}