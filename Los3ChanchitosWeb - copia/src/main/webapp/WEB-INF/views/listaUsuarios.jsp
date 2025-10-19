<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="java.util.List" %>
<%@ page import="com.los3chanchitosweb.model.Usuario" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Usuarios Registrados</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <style>
        body { padding-top: 20px; background-color: #f8f9fa; }
        .container { max-width: 800px; margin-top: 50px; }
        .card { border-radius: 10px; box-shadow: 0 4px 8px rgba(0,0,0,0.1); }
        .card-header { background-color: #007bff; color: white; border-top-left-radius: 10px; border-top-right-radius: 10px; }
        .table thead th { background-color: #007bff; color: white; }
        .btn-logout { margin-top: 20px; }
    </style>
</head>
<body>
<div class="container">
    <div class="card">
        <div class="card-header d-flex justify-content-between align-items-center">
            <h2 class="mb-0 text-center flex-grow-1">Usuarios Registrados</h2>
            <a href="${pageContext.request.contextPath}/logout" class="btn btn-danger btn-sm">Cerrar Sesión</a>
        </div>
        <div class="card-body">
            <table class="table table-bordered table-hover">
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Nombre</th>
                    <th>Email</th>
                    <th>Rol</th>
                </tr>
                </thead>
                <tbody>
                <%
                    List<com.los3chanchitosweb.model.Usuario> usuarios = (List<com.los3chanchitosweb.model.Usuario>) request.getAttribute("listaUsuarios");

                    if (usuarios != null && !usuarios.isEmpty()) {
                        for (com.los3chanchitosweb.model.Usuario usuario : usuarios) {
                %>
                <tr>
                    <td><%= usuario.getId() %></td>
                    <td><%= usuario.getNombre() %></td>
                    <td><%= usuario.getEmail() %></td>
                    <td><%= usuario.getRol() %></td>
                </tr>
                <%
                    }
                } else {
                %>
                <tr>
                    <td colspan="4" class="text-center">No hay usuarios registrados.</td>
                </tr>
                <%
                    }
                %>
                </tbody>
            </table>
        </div>
    </div>
</div>

<script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.5.4/dist/umd/popper.min.js"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
</body>
</html>