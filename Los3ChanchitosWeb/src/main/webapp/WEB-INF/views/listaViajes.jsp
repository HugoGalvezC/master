<%-- Asegúrate de tener los imports necesarios en la parte superior del JSP --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page import="com.los3chanchitosweb.model.Viaje" %>
<%@ page import="java.util.List" %>
<%-- Agrega estos para el mensaje flash --%>
<%
    String mensaje = (String) request.getParameter("mensaje");
    String tipoMensaje = (String) request.getParameter("tipoMensaje");
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Viajes Programados</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <style>
        body { padding-top: 20px; background-color: #f8f9fa; }
        .container { max-width: 1200px; }
        .table thead th { background-color: #007bff; color: white; }
        .alert { margin-top: 20px; }
    </style>
</head>
<body>
<div class="container">
    <h2 class="text-center mb-4">Viajes Programados</h2>

    <%-- Mostrar mensajes de éxito o error --%>
    <% if (mensaje != null && !mensaje.isEmpty()) { %>
    <div class="alert alert-<%= "success".equals(tipoMensaje) ? "success" : "danger" %> alert-dismissible fade show" role="alert">
        <%= mensaje %>
        <button type="button" class="close" data-dismiss="alert" aria-label="Close">
            <span aria-hidden="true">&times;</span>
        </button>
    </div>
    <% } %>

    <div class="mb-3">
        <a href="planificarViaje" class="btn btn-primary">Planificar Nuevo Viaje</a>
    </div>

    <table class="table table-bordered table-hover">
        <thead>
        <tr>
            <th>ID Viaje</th>
            <th>Origen</th>
            <th>Destino</th>
            <th>Fecha y Hora de Salida</th>
            <th>Bus (Patente)</th>
            <th>Chofer (Nombre)</th>
            <th>Auxiliar (Nombre)</th>
            <th>Asientos Disponibles</th>
            <th>Estado</th>
            <th>Acciones</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="viaje" items="${viajes}">
            <tr>
                <td><c:out value="${viaje.idViaje}"/></td>
                <td><c:out value="${viaje.origen}"/></td>
                <td><c:out value="${viaje.destino}"/></td>
                <td><fmt:formatDate value="${viaje.fechaHoraSalidaAsDate}" pattern="dd-MM-yyyy HH:mm"/></td>
                <td>
                    <c:out value="${viaje.bus.patente}"/>
                    (<c:out value="${viaje.bus.modelo}"/> - <c:out value="${viaje.bus.capacidadAsientos}"/> asientos)
                </td>
                <td><c:out value="${viaje.chofer.nombre}"/></td>
                <td>
                    <c:choose>
                        <c:when test="${not empty viaje.auxiliar}">
                            <c:out value="${viaje.auxiliar.nombre}"/>
                        </c:when>
                        <c:otherwise>N/A</c:otherwise>
                    </c:choose>
                </td>
                <td><c:out value="${viaje.asientosDisponibles}"/></td>
                <td><c:out value="${viaje.estadoViaje}"/></td>
                <td>
                    <a href="${pageContext.request.contextPath}/reservarViaje?id=${viaje.idViaje}" class="btn btn-success btn-sm">Reservar</a>
                    <a href="${pageContext.request.contextPath}/editarViaje?id=${viaje.idViaje}" class="btn btn-warning btn-sm">Editar</a>
                    <a href="#" onclick="confirmarEliminar(${viaje.idViaje})" class="btn btn-danger btn-sm">Eliminar</a>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>

<script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.5.4/dist/umd/popper.min.js"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
<script>
    function confirmarEliminar(idViaje) {
        if (confirm("¿Está seguro de que desea eliminar este viaje?")) {
            window.location.href = "${pageContext.request.contextPath}/eliminarViaje?id=" + idViaje;
        }
    }
</script>
</body>
</html>