<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="com.los3chanchitosweb.model.Viaje" %>
<%@ page import="com.los3chanchitosweb.model.Usuario" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Vender Pasaje - Los 3 Chanchitos</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body {
            background-color: #f8f9fa;
        }
        .container {
            max-width: 800px;
            margin-top: 50px;
            background-color: #ffffff;
            padding: 30px;
            border-radius: 8px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
        }
        h2 {
            color: #007bff;
            margin-bottom: 30px;
            text-align: center;
        }
        .form-label {
            font-weight: bold;
        }
        .btn-primary {
            background-color: #007bff;
            border-color: #007bff;
        }
        .btn-primary:hover {
            background-color: #0056b3;
            border-color: #0056b3;
        }
        .alert {
            margin-top: 20px;
        }
    </style>
</head>
<body>
<%
    // Obtener el usuario de la sesión para mostrar su nombre
    Usuario loggedInUser = (Usuario) session.getAttribute("loggedInUser");
    String userName = (loggedInUser != null) ? loggedInUser.getNombre() : "Invitado";

    // Obtener lista de viajes del request
    List<Viaje> viajes = (List<Viaje>) request.getAttribute("viajes");
    if (viajes == null) {
        viajes = new java.util.ArrayList<>(); // Evitar NullPointerException
    }

    // Recuperar mensajes de error o éxito del request si existen
    String mensaje = (String) request.getAttribute("mensaje");
    String tipoMensaje = (String) request.getAttribute("tipoMensaje");
%>

<div class="container">
    <div class="d-flex justify-content-between align-items-center mb-4">
        <h2 class="mb-0">Vender Pasaje</h2>
        <div class="d-flex align-items-center">
            <span class="me-3">Bienvenido, <strong><%= userName %></strong></span>
            <a href="<%= request.getContextPath() %>/logout" class="btn btn-danger btn-sm">Cerrar Sesión</a>
        </div>
    </div>

    <% if (mensaje != null && !mensaje.isEmpty()) { %>
    <div class="alert alert-<%= "success".equals(tipoMensaje) ? "success" : "danger" %> alert-dismissible fade show" role="alert">
        <%= mensaje %>
        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
    </div>
    <% } %>

    <form action="<%= request.getContextPath() %>/venderPasaje" method="POST">
        <h3>Datos del Pasajero</h3>
        <div class="mb-3">
            <label for="nombrePasajero" class="form-label">Nombre:</label>
            <input type="text" class="form-control" id="nombrePasajero" name="nombrePasajero" required>
        </div>
        <div class="mb-3">
            <label for="apellidoPasajero" class="form-label">Apellido:</label>
            <input type="text" class="form-control" id="apellidoPasajero" name="apellidoPasajero" required>
        </div>
        <div class="mb-3">
            <label for="dniPasajero" class="form-label">DNI:</label>
            <input type="text" class="form-control" id="dniPasajero" name="dniPasajero" required>
        </div>
        <div class="mb-3">
            <label for="emailPasajero" class="form-label">Email (Opcional):</label>
            <input type="email" class="form-control" id="emailPasajero" name="emailPasajero">
        </div>
        <div class="mb-3">
            <label for="telefonoPasajero" class="form-label">Teléfono (Opcional):</label>
            <input type="tel" class="form-control" id="telefonoPasajero" name="telefonoPasajero">
        </div>

        <h3 class="mt-4">Datos del Viaje</h3>
        <div class="mb-3">
            <label for="idViaje" class="form-label">Seleccionar Viaje:</label>
            <select class="form-select" id="idViaje" name="idViaje" required>
                <option value="">-- Seleccione un Viaje --</option>
                <% for (Viaje viaje : viajes) { %>
                <option value="<%= viaje.getIdViaje() %>">
                    ID: <%= viaje.getIdViaje() %> - <%= viaje.getOrigen() %> a <%= viaje.getDestino() %> - <%= viaje.getFechaHoraSalida() %> - Asientos Disp.: <%= viaje.getAsientosDisponibles() %>
                </option>
                <% } %>
                <% if (viajes.isEmpty()) { %>
                <option value="" disabled>No hay viajes disponibles.</option>
                <% } %>
            </select>
        </div>
        <div class="mb-3">
            <label for="cantidadPasajes" class="form-label">Cantidad de Pasajes:</label>
            <input type="number" class="form-control" id="cantidadPasajes" name="cantidadPasajes" min="1" value="1" required>
        </div>

        <button type="submit" class="btn btn-primary w-100 mt-4">Registrar Venta</button>
        <a href="<%= request.getContextPath() %>/usuarios" class="btn btn-secondary w-100 mt-2">Volver a la lista de usuarios</a>
    </form>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
