<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Dashboard del Chofer</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css"> <%-- Asume que tienes un archivo CSS --%>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f4f4f4;
            margin: 0;
            padding: 20px;
            color: #333;
        }
        .container {
            max-width: 900px;
            margin: 20px auto;
            background-color: #fff;
            padding: 30px;
            border-radius: 8px;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
        }
        h2 {
            color: #0056b3;
            text-align: center;
            margin-bottom: 25px;
        }
        .info-card {
            background-color: #e9ecef;
            padding: 15px;
            border-radius: 5px;
            margin-bottom: 20px;
            border-left: 5px solid #007bff;
        }
        .info-card h3 {
            margin-top: 0;
            color: #007bff;
        }
        .info-card p {
            margin: 5px 0;
        }
        .action-buttons {
            text-align: center;
            margin-top: 30px;
        }
        .action-buttons a, .action-buttons button {
            display: inline-block;
            background-color: #28a745;
            color: white;
            padding: 12px 25px;
            border-radius: 5px;
            text-decoration: none;
            margin: 0 10px;
            border: none;
            cursor: pointer;
            font-size: 16px;
            transition: background-color 0.3s ease;
        }
        .action-buttons a:hover, .action-buttons button:hover {
            background-color: #218838;
        }
        .alert-message {
            padding: 10px;
            border-radius: 5px;
            margin-bottom: 15px;
            text-align: center;
        }
        .alert-success {
            background-color: #d4edda;
            color: #155724;
            border: 1px solid #c3e6cb;
        }
        .alert-error {
            background-color: #f8d7da;
            color: #721c24;
            border: 1px solid #f5c6cb;
        }
        .alert-warning {
            background-color: #fff3cd;
            color: #856404;
            border: 1px solid #ffeeba;
        }
    </style>
</head>
<body>
<%-- Navbar (opcional, si decides crearla después) --%>
<%-- Si tienes un navbar.jsp o similar, puedes incluirlo aquí --%>
<%-- <jsp:include page="/WEB-INF/views/navbar.jsp" /> --%>

<div class="container">
    <h2>Bienvenido, <c:out value="${loggedInUser.nombre}" /> (Chofer)</h2>

    <c:if test="${not empty mensaje}">
        <div class="alert-message alert-${tipoMensaje}">
            <c:out value="${mensaje}" />
        </div>
    </c:if>

    <div class="info-card">
        <h3>Tu Próximo Viaje Asignado:</h3>
        <c:choose>
            <c:when test="${not empty viajeAsignado}">
                <p><strong>ID Viaje:</strong> <c:out value="${viajeAsignado.idViaje}" /></p>
                <p><strong>Origen:</strong> <c:out value="${viajeAsignado.origen}" /></p>
                <p><strong>Destino:</strong> <c:out value="${viajeAsignado.destino}" /></p>
                <p><strong>Fecha y Hora:</strong> <c:out value="${viajeAsignado.fechaHoraSalida}" /></p>
                <c:if test="${not empty viajeAsignado.bus}">
                    <p><strong>Bus Asignado:</strong> <c:out value="${viajeAsignado.bus.patente}" /> (<c:out value="${viajeAsignado.bus.modelo}" /> - <c:out value="${viajeAsignado.bus.capacidadAsientos}" /> asientos)</p>
                </c:if>
                <c:if test="${empty viajeAsignado.bus}">
                    <p><strong>Bus Asignado:</strong> N/A</p>
                </c:if>
                <p><strong>Auxiliar Asignado:</strong>
                    <c:choose>
                        <c:when test="${not empty viajeAsignado.auxiliar}">
                            <c:out value="${viajeAsignado.auxiliar.nombre}" />
                        </c:when>
                        <c:otherwise>N/A</c:otherwise>
                    </c:choose>
                </p>
                <p><strong>Estado del Viaje:</strong> <c:out value="${viajeAsignado.estadoViaje}" /></p>

                <p><strong>Estado de Asistencia:</strong>
                    <c:choose>
                        <c:when test="${not empty viajeAsignado.estadoAsistenciaChofer}">
                            <c:out value="${viajeAsignado.estadoAsistenciaChofer}" />
                            <c:if test="${not empty viajeAsignado.fechaHoraAsistenciaChofer}">
                                (Marcado el: <c:out value="${viajeAsignado.fechaHoraAsistenciaChofer}" />)
                            </c:if>
                        </c:when>
                        <c:otherwise>Pendiente</c:otherwise>
                    </c:choose>
                </p>
                <div class="action-buttons">
                        <%-- Formulario para Marcar Asistencia --%>
                    <form action="${pageContext.request.contextPath}/MarcarAsistenciaServlet" method="post" style="display:inline;">
                        <input type="hidden" name="idViaje" value="${viajeAsignado.idViaje}">
                        <input type="hidden" name="idChofer" value="${loggedInUser.id}"> <%-- Asume que loggedInUser.id es el id_chofer --%>
                        <button type="submit"
                                <c:if test="${viajeAsignado.estadoAsistenciaChofer == 'Asistió'}">disabled</c:if>>
                            Marcar Asistencia
                        </button>
                    </form>
                        <%-- Enlace para ver la Lista de Pasajeros --%>
                    <a href="${pageContext.request.contextPath}/ListaPasajerosServlet?idViaje=${viajeAsignado.idViaje}">Ver Lista de Pasajeros</a>
                </div>
            </c:when>
            <c:otherwise>
                <p>No tienes ningún viaje asignado próximamente.</p>
            </c:otherwise>
        </c:choose>
    </div>

    <div class="action-buttons">
        <a href="${pageContext.request.contextPath}/logout">Cerrar Sesión</a>
    </div>
</div>
</body>
</html>