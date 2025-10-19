<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Registro de Usuario</title>
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
        .register-container {
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
        input[type="text"],
        input[type="email"],
        input[type="password"],
        select {
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
        .login-link {
            margin-top: 15px;
            font-size: 14px;
            color: #666;
        }
        .login-link a {
            color: #007bff;
            text-decoration: none;
        }
        .login-link a:hover {
            text-decoration: underline;
        }
    </style>
</head>
<body>
<div class="register-container">
    <h2>Registro de Usuario</h2>

    <%-- Mostrar mensaje de error o éxito si existe --%>
    <%
        String mensaje = (String) request.getAttribute("mensaje");
        if (mensaje != null) {
            String tipoMensaje = (String) request.getAttribute("tipoMensaje"); // "error" o "success"
    %>
    <div class="message <%= tipoMensaje != null ? tipoMensaje : "" %>">
        <%= mensaje %>
    </div>
    <%
        }
    %>

    <form action="<%= request.getContextPath() %>/registro" method="post">
        <div class="form-group">
            <label for="nombre">Nombre:</label>
            <input type="text" id="nombre" name="nombre" required>
        </div>
        <div class="form-group">
            <label for="email">Email:</label>
            <input type="email" id="email" name="email" required>
        </div>
        <div class="form-group">
            <label for="password">Contraseña:</label>
            <input type="password" id="password" name="password" required>
        </div>
        <div class="form-group">
            <label for="rol">Rol:</label>
            <select id="rol" name="rol" required>
                <option value="cliente">Cliente</option>
                <option value="vendedor">Vendedor</option>
                <option value="chofer">Chofer</option>
                <option value="jefe_operaciones">Jefe de Operaciones</option>
            </select>
        </div>
        <button type="submit">Registrarse</button>
    </form>
    <div class="login-link">
        ¿Ya tienes una cuenta? <a href="<%= request.getContextPath() %>/login">Inicia Sesión aquí</a>
    </div>
</div>
</body>
</html>