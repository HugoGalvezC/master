<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Error</title>
    <link href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<div class="container mt-5">
    <div class="alert alert-danger text-center" role="alert">
        <h4 class="alert-heading">¡Ha ocurrido un error!</h4>
        <p>Lo sentimos, ha ocurrido un problema. Por favor, inténtelo de nuevo más tarde o contacte al soporte.</p>
        <hr>
        <p class="mb-0">Mensaje de error: ${pageContext.exception.message}</p>
        <p class="mb-0">URL solicitada: ${pageContext.request.requestURI}</p>
    </div>
    <div class="text-center">
        <a href="index.jsp" class="btn btn-primary">Volver al Inicio</a>
    </div>
</div>
</body>
</html>