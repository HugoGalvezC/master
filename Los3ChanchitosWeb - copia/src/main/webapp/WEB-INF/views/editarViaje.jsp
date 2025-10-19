<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Editar Viaje</title>
    <link href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" rel="stylesheet">
    <style>
        .form-container {
            max-width: 600px;
            margin: 50px auto;
            padding: 30px;
            border: 1px solid #ddd;
            border-radius: 8px;
            box-shadow: 0 0 10px rgba(0,0,0,0.1);
        }
    </style>
</head>
<body>
<div class="container form-container">
    <h1 class="mb-4 text-center">Editar Viaje</h1>

    <c:if test="${not empty mensajeError}">
        <div class="alert alert-danger" role="alert">
                ${mensajeError}
        </div>
    </c:if>

    <form action="${pageContext.request.contextPath}/editarViaje" method="post">
        <input type="hidden" name="idViaje" value="${viaje.idViaje}">

        <div class="form-group">
            <label for="origen">Origen:</label>
            <input type="text" class="form-control" id="origen" name="origen" value="${viaje.origen}" required>
        </div>

        <div class="form-group">
            <label for="destino">Destino:</label>
            <input type="text" class="form-control" id="destino" name="destino" value="${viaje.destino}" required>
        </div>

        <div class="form-group">
            <label for="fechaHoraSalida">Fecha y Hora de Salida:</label>
            <%-- IMPORTANTE: El formato de datetime-local es 'yyyy-MM-ddTHH:mm' --%>
            <input type="datetime-local" class="form-control" id="fechaHoraSalida" name="fechaHoraSalida"
                   value="<fmt:formatDate value="${viaje.fechaHoraSalidaAsDate}" pattern="yyyy-MM-dd'T'HH:mm"/>" required>
        </div>

        <div class="form-group">
            <label for="idBus">Bus:</label>
            <select class="form-control" id="idBus" name="idBus" required>
                <c:forEach var="bus" items="${buses}">
                    <option value="${bus.idBus}" ${viaje.bus.idBus == bus.idBus ? 'selected' : ''}>
                            ${bus.patente} (${bus.modelo} - ${bus.capacidadAsientos} asientos)
                    </option>
                </c:forEach>
            </select>
        </div>

        <div class="form-group">
            <label for="idChofer">Chofer:</label>
            <select class="form-control" id="idChofer" name="idChofer" required>
                <c:forEach var="chofer" items="${choferes}">
                    <option value="${chofer.idChofer}" ${viaje.chofer.idChofer == chofer.idChofer ? 'selected' : ''}>
                            ${chofer.nombre} (Licencia: ${chofer.licencia})
                    </option>
                </c:forEach>
            </select>
        </div>

        <div class="form-group">
            <label for="idAuxiliar">Auxiliar (Opcional):</label>
            <select class="form-control" id="idAuxiliar" name="idAuxiliar">
                <option value="0" ${viaje.auxiliar == null ? 'selected' : ''}>Ninguno</option>
                <c:forEach var="auxiliar" items="${auxiliares}">
                    <option value="${auxiliar.idAuxiliar}" ${viaje.auxiliar != null && viaje.auxiliar.idAuxiliar == auxiliar.idAuxiliar ? 'selected' : ''}>
                            ${auxiliar.nombre}
                    </option>
                </c:forEach>
            </select>
        </div>

        <div class="form-group">
            <label for="asientosDisponibles">Asientos Disponibles:</label>
            <input type="number" class="form-control" id="asientosDisponibles" name="asientosDisponibles" value="${viaje.asientosDisponibles}" required min="0">
        </div>

        <div class="form-group">
            <label for="estadoViaje">Estado del Viaje:</label>
            <select class="form-control" id="estadoViaje" name="estadoViaje" required>
                <option value="Programado" ${viaje.estadoViaje == 'Programado' ? 'selected' : ''}>Programado</option>
                <option value="En Curso" ${viaje.estadoViaje == 'En Curso' ? 'selected' : ''}>En Curso</option>
                <option value="Finalizado" ${viaje.estadoViaje == 'Finalizado' ? 'selected' : ''}>Finalizado</option>
                <option value="Cancelado" ${viaje.estadoViaje == 'Cancelado' ? 'selected' : ''}>Cancelado</option>
            </select>
        </div>

        <button type="submit" class="btn btn-primary btn-block">Guardar Cambios</button>
        <a href="${pageContext.request.contextPath}/listarViajes" class="btn btn-secondary btn-block mt-2">Cancelar</a>
    </form>
</div>

<script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.9.2/dist/umd/popper.min.js"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
</body>
</html>