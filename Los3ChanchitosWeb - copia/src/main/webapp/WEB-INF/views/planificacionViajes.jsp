<%@ page import="java.util.List" %>
<%@ page import="com.los3chanchitosweb.model.Bus" %>
<%@ page import="com.los3chanchitosweb.model.Chofer" %>
<%@ page import="com.los3chanchitosweb.model.Auxiliar" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Planificar Nuevo Viaje</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f4f4f4;
            display: flex;
            justify-content: center;
            align-items: center;
            min-height: 100vh;
            margin: 0;
        }
        .container {
            background-color: #fff;
            padding: 30px;
            border-radius: 8px;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
            width: 100%;
            max-width: 500px;
        }
        h2 {
            color: #333;
            text-align: center;
            margin-bottom: 25px;
        }
        .form-group {
            margin-bottom: 15px;
        }
        .form-group label {
            display: block;
            margin-bottom: 5px;
            font-weight: bold;
            color: #555;
        }
        .form-group input[type="text"],
        .form-group input[type="datetime-local"],
        .form-group select {
            width: calc(100% - 22px); /* Ajusta para padding y borde */
            padding: 10px;
            border: 1px solid #ccc;
            border-radius: 4px;
            box-sizing: border-box; /* Incluye padding y borde en el ancho */
        }
        button {
            width: 100%;
            padding: 12px;
            background-color: #007bff;
            color: white;
            border: none;
            border-radius: 4px;
            font-size: 16px;
            cursor: pointer;
            transition: background-color 0.3s ease;
        }
        button:hover {
            background-color: #0056b3;
        }
        .message {
            text-align: center;
            margin-top: 15px;
            padding: 10px;
            border-radius: 5px;
        }
        .message.success {
            background-color: #d4edda;
            color: #155724;
            border: 1px solid #c3e6cb;
        }
        .message.error {
            background-color: #f8d7da;
            color: #721c24;
            border: 1px solid #f5c6cb;
        }
        .link-volver {
            display: block;
            text-align: center;
            margin-top: 20px;
            text-decoration: none;
            color: #007bff;
        }
    </style>
</head>
<body>
<div class="container">
    <h2>Planificar Nuevo Viaje</h2>

    <%
        String mensaje = (String) request.getAttribute("mensaje");
        String tipoMensaje = (String) request.getAttribute("tipoMensaje");
        if (mensaje != null) {
    %>
    <div class="message <%= tipoMensaje %>">
        <%= mensaje %>
    </div>
    <%
        }
    %>

    <form action="<%= request.getContextPath() %>/planificarViaje" method="post">
        <div class="form-group">
            <label for="origen">Origen:</label>
            <input type="text" id="origen" name="origen" required>
        </div>
        <div class="form-group">
            <label for="destino">Destino:</label>
            <input type="text" id="destino" name="destino" required>
        </div>
        <div class="form-group">
            <label for="fechaHoraSalida">Fecha y Hora de Salida:</label>
            <input type="datetime-local" id="fechaHoraSalida" name="fechaHoraSalida" required>
        </div>
        <div class="form-group">
            <label for="idBus">Bus:</label>
            <select id="idBus" name="idBus" required>
                <option value="">Seleccione un bus</option>
                <%
                    List<Bus> buses = (List<Bus>) request.getAttribute("buses");
                    if (buses != null) {
                        for (Bus bus : buses) {
                %>
                <option value="<%= bus.getIdBus() %>"><%= bus.getPatente() %> - <%= bus.getModelo() %> (<%= bus.getCapacidadAsientos() %> asientos)</option>
                <%
                        }
                    }
                %>
            </select>
        </div>
        <div class="form-group">
            <label for="idChofer">Chofer:</label>
            <select id="idChofer" name="idChofer" required>
                <option value="">Seleccione un chofer</option>
                <%
                    List<Chofer> choferes = (List<Chofer>) request.getAttribute("choferes");
                    if (choferes != null) {
                        for (Chofer chofer : choferes) {
                %>
                <option value="<%= chofer.getIdChofer() %>"><%= chofer.getNombre() %> (Licencia: <%= chofer.getLicencia() %>)</option>
                <%
                        }
                    }
                %>
            </select>
        </div>
        <div class="form-group">
            <label for="idAuxiliar">Auxiliar (Opcional):</label>
            <select id="idAuxiliar" name="idAuxiliar">
                <option value="">Ninguno</option>
                <%
                    List<Auxiliar> auxiliares = (List<Auxiliar>) request.getAttribute("auxiliares");
                    if (auxiliares != null) {
                        for (Auxiliar auxiliar : auxiliares) {
                %>
                <option value="<%= auxiliar.getIdAuxiliar() %>"><%= auxiliar.getNombre() %></option>
                <%
                        }
                    }
                %>
            </select>
        </div>
        <button type="submit">Planificar Viaje</button>
    </form>
    <a href="<%= request.getContextPath() %>/usuarios" class="link-volver">Volver a la lista de usuarios</a> <%-- O a un dashboard de jefe de operaciones --%>
</div>
</body>
</html>