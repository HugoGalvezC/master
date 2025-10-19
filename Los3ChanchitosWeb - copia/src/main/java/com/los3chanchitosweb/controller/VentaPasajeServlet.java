package com.los3chanchitosweb.controller;

import com.los3chanchitosweb.dao.PasajeroDAO;
import com.los3chanchitosweb.dao.VentaDAO;
import com.los3chanchitosweb.dao.PasajeDAO;
import com.los3chanchitosweb.dao.ViajeDAO;
import com.los3chanchitosweb.model.Pasajero;
import com.los3chanchitosweb.model.Venta;
import com.los3chanchitosweb.model.Pasaje;
import com.los3chanchitosweb.model.Viaje;
import com.los3chanchitosweb.model.Usuario; // Necesario para obtener el ID del vendedor

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.net.URLEncoder; // Para codificar URLs
import java.sql.Connection; // Para control de transacciones
import com.los3chanchitosweb.util.DBConnection; // Para obtener la conexión a la DB
import java.sql.Statement; // Para Statement.RETURN_GENERATED_KEYS (aunque no se usa directamente aquí, es una buena práctica tenerla si manejas IDs generados)
import java.sql.ResultSet; // Para ResultSet (aunque no se usa directamente aquí)
import java.sql.Timestamp; // Para Timestamp (aunque no se usa directamente aquí)
import java.sql.Types; // Para Types.INTEGER (aunque no se usa directamente aquí)


@WebServlet("/venderPasaje") // URL para este Servlet
public class VentaPasajeServlet extends HttpServlet {

    private PasajeroDAO pasajeroDAO;
    private VentaDAO ventaDAO;
    private PasajeDAO pasajeDAO;
    private ViajeDAO viajeDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        pasajeroDAO = new PasajeroDAO();
        ventaDAO = new VentaDAO();
        pasajeDAO = new PasajeDAO();
        viajeDAO = new ViajeDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // --- Lógica de Seguridad: Verificar la sesión y el usuario autenticado ---
        HttpSession session = request.getSession(false);
        Usuario loggedInUser = null;
        if (session != null) {
            loggedInUser = (Usuario) session.getAttribute("loggedInUser");
        }

        // Si no hay sesión o no hay usuario logueado, redirige al login
        if (session == null || loggedInUser == null) {
            System.out.println("DEBUG: Usuario no autenticado para /venderPasaje. Redirigiendo a login.");
            response.sendRedirect(request.getContextPath() + "/login?reason=auth_required");
            return;
        }

        // --- Restricción de Rol: Solo el VENDEDOR puede acceder a esta funcionalidad ---
        // Asumiendo que el rol del vendedor es "vendedor"
        if (!"vendedor".equalsIgnoreCase(loggedInUser.getRol())) {
            request.setAttribute("mensaje", "Acceso denegado. Solo los vendedores pueden ingresar ventas.");
            request.setAttribute("tipoMensaje", "error");
            System.out.println("DEBUG: Intento de acceso a /venderPasaje por usuario con rol " + loggedInUser.getRol());
            request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response); // O redirige a un dashboard
            return;
        }
        // --- Fin Lógica de Seguridad y Rol ---

        try {
            // Cargar la lista de viajes disponibles para que el vendedor pueda seleccionar
            List<Viaje> viajes = viajeDAO.obtenerTodosLosViajes();
            request.setAttribute("viajes", viajes);

            // Forward al JSP para mostrar el formulario de venta
            request.getRequestDispatcher("/WEB-INF/views/crearVentaPasaje.jsp").forward(request, response);
        } catch (SQLException e) {
            System.err.println("ERROR: SQLException al cargar viajes para venta: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("mensaje", "Error al cargar la lista de viajes disponibles.");
            request.setAttribute("tipoMensaje", "error");
            request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response); // O redirige a un dashboard
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // --- Lógica de Seguridad: Verificar la sesión y el usuario autenticado ---
        HttpSession session = request.getSession(false);
        Usuario loggedInUser = null;
        if (session != null) {
            loggedInUser = (Usuario) session.getAttribute("loggedInUser");
        }

        if (session == null || loggedInUser == null || !"vendedor".equalsIgnoreCase(loggedInUser.getRol())) {
            response.sendRedirect(request.getContextPath() + "/login?reason=auth_required");
            return;
        }
        // --- Fin Lógica de Seguridad y Rol ---

        String mensaje = "";
        String tipoMensaje = "error";
        int idVentaGenerado = -1; // Inicializar a un valor inválido por defecto

        Connection conn = null; // Para control de transacciones
        try {
            // Iniciar una transacción para asegurar la consistencia de los datos
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false); // Deshabilitar auto-commit

            // 1. Obtener datos del formulario
            String nombrePasajero = request.getParameter("nombrePasajero");
            String apellidoPasajero = request.getParameter("apellidoPasajero");
            String dniPasajero = request.getParameter("dniPasajero");
            String emailPasajero = request.getParameter("emailPasajero");
            String telefonoPasajero = request.getParameter("telefonoPasajero");
            String idViajeParam = request.getParameter("idViaje");
            String cantidadPasajesParam = request.getParameter("cantidadPasajes");

            if (nombrePasajero == null || nombrePasajero.isEmpty() ||
                    apellidoPasajero == null || apellidoPasajero.isEmpty() ||
                    dniPasajero == null || dniPasajero.isEmpty() ||
                    idViajeParam == null || idViajeParam.isEmpty() ||
                    cantidadPasajesParam == null || cantidadPasajesParam.isEmpty()) {
                mensaje = "Todos los campos obligatorios deben ser completados.";
                throw new IllegalArgumentException(mensaje);
            }

            int idViaje = Integer.parseInt(idViajeParam);
            int cantidadPasajes = Integer.parseInt(cantidadPasajesParam);

            // 2. Buscar o crear el Pasajero
            Pasajero pasajero = pasajeroDAO.buscarPasajeroPorDNI(dniPasajero);
            int idPasajero;
            if (pasajero == null) {
                // Si el pasajero no existe, insertarlo
                pasajero = new Pasajero(nombrePasajero, apellidoPasajero, dniPasajero, emailPasajero, telefonoPasajero);
                idPasajero = pasajeroDAO.insertarPasajero(pasajero);
                if (idPasajero == -1) {
                    throw new SQLException("Error al insertar el nuevo pasajero.");
                }
            } else {
                // Si el pasajero ya existe, usar su ID
                idPasajero = pasajero.getIdPasajero();
                // Opcional: Actualizar los datos del pasajero si han cambiado. Aunque por DNI no debería cambiar.
            }

            // 3. Obtener el Viaje y verificar disponibilidad
            Viaje viaje = viajeDAO.obtenerViajePorId(idViaje);
            if (viaje == null) {
                mensaje = "El viaje seleccionado no existe.";
                throw new IllegalArgumentException(mensaje);
            }
            if (viaje.getAsientosDisponibles() < cantidadPasajes) {
                mensaje = "No hay suficientes asientos disponibles para la cantidad solicitada. Disponibles: " + viaje.getAsientosDisponibles();
                throw new IllegalArgumentException(mensaje);
            }

            // 4. Calcular el precio total de la venta (precio de un pasaje * cantidad)
            // Asumimos un precio base por pasaje. Ajusta este valor según tu lógica de negocio.
            double precioUnitario = 50.0; // <<-- AJUSTA ESTE PRECIO SEGÚN TUS NECESIDADES O CÁLCULOS
            double montoTotalVenta = precioUnitario * cantidadPasajes;

            // 5. Crear la Venta
            Venta nuevaVenta = new Venta(loggedInUser.getId(), montoTotalVenta, "Completada");
            idVentaGenerado = ventaDAO.insertarVenta(nuevaVenta); // Capturamos el ID de la venta generada
            if (idVentaGenerado == -1) {
                throw new SQLException("Error al registrar la venta principal.");
            }

            // 6. Crear los Pasajes individuales para esta Venta
            for (int i = 0; i < cantidadPasajes; i++) {
                Pasaje nuevoPasaje = new Pasaje(idVentaGenerado, idViaje, idPasajero, precioUnitario, null, "Vendido");
                int idPasajeGenerado = pasajeDAO.insertarPasaje(nuevoPasaje);
                if (idPasajeGenerado == -1) {
                    throw new SQLException("Error al registrar el pasaje #" + (i + 1));
                }
            }

            // 7. Actualizar los asientos disponibles en el Viaje
            viaje.setAsientosDisponibles(viaje.getAsientosDisponibles() - cantidadPasajes);
            boolean actualizacionViajeExitosa = viajeDAO.actualizarViaje(viaje);
            if (!actualizacionViajeExitosa) {
                throw new SQLException("Error al actualizar los asientos del viaje.");
            }

            // Si todo fue bien, confirmar la transacción
            conn.commit();
            mensaje = "Venta de pasaje(s) realizada con éxito. ID de Venta: " + idVentaGenerado;
            tipoMensaje = "success";
            System.out.println("DEBUG: Venta exitosa. ID Venta: " + idVentaGenerado + ", Pasajero DNI: " + dniPasajero + ", Viaje ID: " + idViaje);

        } catch (IllegalArgumentException e) {
            mensaje = e.getMessage();
            tipoMensaje = "error";
            System.err.println("ERROR (Validación): " + e.getMessage());
            try {
                if (conn != null) conn.rollback(); // Deshacer si hay error
            } catch (SQLException rbEx) {
                System.err.println("ERROR: Rollback fallido: " + rbEx.getMessage());
            }
        } catch (SQLException e) {
            mensaje = "Error en la base de datos al procesar la venta: " + e.getMessage();
            tipoMensaje = "error";
            System.err.println("ERROR (SQL): " + e.getMessage());
            e.printStackTrace();
            try {
                if (conn != null) conn.rollback(); // Deshacer si hay error
            } catch (SQLException rbEx) {
                System.err.println("ERROR: Rollback fallido: " + rbEx.getMessage());
            }
        } catch (Exception e) {
            mensaje = "Ocurrió un error inesperado al procesar la venta: " + e.getMessage();
            tipoMensaje = "error";
            System.err.println("ERROR (General): " + e.getMessage());
            e.printStackTrace();
            try {
                if (conn != null) conn.rollback(); // Deshacer si hay error
            } catch (SQLException rbEx) {
                System.err.println("ERROR: Rollback fallido: " + rbEx.getMessage());
            }
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true); // Restaurar auto-commit a true
                    conn.close(); // Cerrar conexión
                } catch (SQLException closeEx) {
                    System.err.println("ERROR: Error al cerrar la conexión de la BD: " + closeEx.getMessage());
                }
            }
        }

        // --- LÓGICA DE REDIRECCIÓN FINAL ---
        if ("success".equals(tipoMensaje)) {
            // Si la venta fue exitosa, redirigir a la página de emisión de pasajes
            // Pasamos el ID de la venta para que el EmisionPasajeServlet sepa qué pasajes mostrar
            // Asegúrate de que idVentaGenerado tenga el valor correcto aquí.
            response.sendRedirect(request.getContextPath() + "/emitirPasaje?idVenta=" + idVentaGenerado);
        } else {
            // Si hubo algún error, redirigir a listarViajes o a la misma página de venta con el mensaje de error
            String encodedMensaje = URLEncoder.encode(mensaje, "UTF-8");
            String encodedTipoMensaje = URLEncoder.encode(tipoMensaje, "UTF-8");
            // Puedes elegir entre redirigir a listarViajes (si es una página general de información)
            // o a la misma página de venta (crearVentaPasaje.jsp) para que el usuario corrija.
            // Por simplicidad, mantendremos la redirección a listarViajes con el mensaje de error.
            response.sendRedirect(request.getContextPath() + "/listarViajes?mensaje=" + encodedMensaje + "&tipoMensaje=" + encodedTipoMensaje);
        }
        // --- FIN DE LÓGICA DE REDIRECCIÓN FINAL ---
    }
}