<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Iniciar Sesi&oacute;n</title>
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
        .login-container {
            background-color: #fff;
            padding: 30px;
            border-radius: 8px;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
            width: 350px;
            text-align: center;
        }
        h2 {
            color: #333;
            margin-bottom: 25px;
        }
        .form-group {
            margin-bottom: 20px;
            text-align: left;
        }
        label {
            display: block;
            margin-bottom: 8px;
            color: #555;
            font-weight: bold;
        }
        input[type="email"],
        input[type="password"] {
            width: 100%;
            padding: 12px;
            border: 1px solid #ddd;
            border-radius: 4px;
            box-sizing: border-box; /* Ensures padding doesn't increase width */
            font-size: 16px;
        }
        button {
            background-color: #007bff;
            color: white;
            padding: 12px 20px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            font-size: 18px;
            width: 100%;
            transition: background-color 0.3s ease;
        }
        button:hover {
            background-color: #0056b3;
        }
        .register-link {
            margin-top: 15px;
            font-size: 14px;
            color: #666;
        }
        .register-link a {
            color: #007bff;
            text-decoration: none;
        }
        .register-link a:hover {
            text-decoration: underline;
        }
        .message {
            margin-top: 20px;
            padding: 10px;
            border-radius: 4px;
            font-weight: bold;
        }
        .message.error {
            background-color: #f8d7da;
            color: #721c24;
            border: 1px solid #f5c6cb;
        }
        .message.success {
            background-color: #d4edda;
            color: #155724;
            border: 1px solid #c3e6cb;
        }
    </style>
</head>
<body>
<div class="login-container">
    <h2>Iniciar Sesión</h2>

    <%-- Mostrar mensaje de error o éxito del registro --%>
    <%
        String registroMensaje = (String) request.getAttribute("mensaje");
        String registroTipoMensaje = (String) request.getAttribute("tipoMensaje");
        if (registroMensaje != null) {
    %>
    <div class="message <%= registroTipoMensaje != null ? registroTipoMensaje : "" %>">
        <%= registroMensaje %>
    </div>
    <%
        }
    %>

    <%-- *** CAMBIO AQUÍ: Mostrar mensaje de login (ej. acceso denegado) *** --%>
    <%
        String loginMessage = (String) request.getAttribute("loginMessage");
        if (loginMessage != null && !loginMessage.isEmpty()) {
    %>
    <div class="message error"> <%-- Usamos 'error' para mensajes de login/acceso --%>
        <%= loginMessage %>
    </div>
    <%
        }
    %>


    <form action="<%= request.getContextPath() %>/login" method="post">
        <div class="form-group">
            <label for="email">Email:</label>
            <input type="email" id="email" name="email" value="<%= request.getAttribute("email") != null ? request.getAttribute("email") : "" %>" required>
        </div>
        <div class="form-group">
            <label for="password">Contraseña:</label>
            <input type="password" id="password" name="password" required>
        </div>
        <button type="submit">Iniciar Sesión</button>
    </form>
    <div class="register-link">
        ¿No tienes cuenta? <a href="<%= request.getContextPath() %>/registro">Regístrate aquí</a>
    </div>
</div>
</body>
</html>