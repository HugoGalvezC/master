<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Emitir Pasaje(s)</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <style>
        body {
            background-color: #f8f9fa;
        }
        .container {
            margin-top: 50px;
        }
        .ticket-card {
            border: 1px solid #dee2e6;
            border-radius: 8px;
            padding: 20px;
            margin-bottom: 30px;
            background-color: #fff;
            box-shadow: 0 4px 8px rgba(0,0,0,0.1);
        }
        .ticket-header {
            background-color: #007bff;
            color: white;
            padding: 10px;
            border-radius: 8px 8px 0 0;
            margin: -20px -20px 20px -20px;
            text-align: center;
        }
        .ticket-detail {
            margin-bottom: 10px;
        }
        .ticket-detail strong {
            display: inline-block;
            width: 120px; /* Ancho fijo para alinear las etiquetas */
        }
        .ticket-footer {
            margin-top: 20px;
            text-align: center;
            border-top: 1px dashed #dee2e6;
            padding-top: 15px;
        }
        .success-message {
            color: green;
            font-weight: bold;
            text-align: center;
            margin-bottom: 20px;
        }
        .warning-message {
            color: orange;
            font-weight: bold;
            text-align: center;
            margin-bottom: 20px;
        }
        .error-message {
            color: red;
            font-weight: bold;
            text-align: center;
            margin-bottom: 20px;
        }
    </style>
</head>
<body>

<div class="container">
    <h2 class="mb-4 text-center">Boletos Emitidos</h2>

    <c:if test="${not empty mensaje}">
        <div class="${tipoMensaje eq 'success' ? 'success-message' : (tipoMensaje eq 'warning' ? 'warning-message' : 'error-message')}">
                ${mensaje}
        </div>
    </c:if>

    <c:choose>
        <c:when test="${not empty pasajesEmitidos}">
            <c:forEach var="pasaje" items="${pasajesEmitidos}">
                <div class="ticket-card">
                    <div class="ticket-header">
                        <h3>PASAJERO: ${pasaje.pasajero.nombre} ${pasaje.pasajero.apellido}</h3>
                    </div>
                    <div class="ticket-detail">
                        <strong>Folio:</strong> ${pasaje.idPasaje}
                    </div>
                    <div class="ticket-detail">
                        <strong>Fecha/Hora Salida:</strong> ${pasaje.viaje.fechaHoraSalida}
                    </div>
                    <div class="ticket-detail">
                        <strong>Origen:</strong> ${pasaje.viaje.origen}
                    </div>
                    <div class="ticket-detail">
                        <strong>Destino:</strong> ${pasaje.viaje.destino}
                    </div>
                    <div class="ticket-detail">
                        <strong>Patente Bus:</strong> ${pasaje.bus.patente} (${pasaje.bus.modelo})
                    </div>
                    <div class="ticket-detail">
                        <strong>Chofer:</strong> ${pasaje.chofer.nombre}
                    </div>
                    <div class="ticket-detail">
                        <strong>Auxiliar:</strong> ${not empty pasaje.auxiliar ? pasaje.auxiliar.nombre : 'N/A'}
                    </div>
                    <div class="ticket-detail">
                        <strong>Precio:</strong> $<fmt:formatNumber value="${pasaje.precio}" type="number" minFractionDigits="2" maxFractionDigits="2"/>
                    </div>
                    <div class="ticket-footer">
                        ¡Gracias por elegir Los 3 Chanchitos!
                    </div>
                </div>
            </c:forEach>
        </c:when>
        <c:otherwise>
            <div class="alert alert-info text-center">
                No hay pasajes para mostrar.
            </div>
        </c:otherwise>
    </c:choose>

    <div class="text-center mt-4">
        <a href="${pageContext.request.contextPath}/listarViajes" class="btn btn-secondary">Volver a Viajes</a>
        <a href="${pageContext.request.contextPath}/venderPasaje" class="btn btn-primary">Realizar Otra Venta</a>
    </div>
</div>

<script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.5.4/dist/umd/popper.min.js"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
</body>
</html>