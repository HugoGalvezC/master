package com.los3chanchitosweb.controller;

import com.los3chanchitosweb.dao.VentaDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/AnularVentaServlet")
public class AnularVentaServlet extends HttpServlet {

    private VentaDAO ventaDAO;

    public void init() {
        ventaDAO = new VentaDAO();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        try {
            // 1. Obtener el ID de la venta desde los parámetros de la URL
            int idVenta = Integer.parseInt(request.getParameter("idVenta"));

            // 2. Llamar al método en el DAO para anular la venta
            boolean anulado = ventaDAO.anularVenta(idVenta);

            if (anulado) {
                // Éxito: Redirigir a la lista de ventas con un mensaje de éxito
                request.getSession().setAttribute("mensaje", "Venta anulada con éxito.");
                request.getSession().setAttribute("tipoMensaje", "success");
            } else {
                // Error: Redirigir con un mensaje de error
                request.getSession().setAttribute("mensaje", "No se pudo anular la venta.");
                request.getSession().setAttribute("tipoMensaje", "danger");
            }
            response.sendRedirect(request.getContextPath() + "/listarVentas.jsp"); // Asegúrate de tener un JSP para listar ventas

        } catch (SQLException | NumberFormatException e) {
            e.printStackTrace();
            request.getSession().setAttribute("mensaje", "Error al procesar la anulación de la venta: " + e.getMessage());
            request.getSession().setAttribute("tipoMensaje", "danger");
            response.sendRedirect(request.getContextPath() + "/error.jsp");
        }
    }
}