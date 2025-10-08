package com.los3chanchitosweb.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    // Credenciales de la base de datos
    private static final String URL = "jdbc:mysql://localhost:3306/los3chanchitosdb";
    private static final String USER = "root"; // Cambia esto si usas un usuario diferente
    private static final String PASSWORD = "admin"; // ¡¡¡CAMBIA ESTO POR TU CONTRASEÑA DE MYSQL!!!

    // Bloque estático para cargar el driver JDBC
    static {
        try {
            // Carga el driver JDBC de MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("Error: No se pudo cargar el driver JDBC de MySQL.");
            e.printStackTrace();
            throw new ExceptionInInitializerError(e); // Lanza un error para detener la aplicación si el driver no carga
        }
    }

    /**
     * Obtiene una conexión a la base de datos.
     * @return Objeto Connection si la conexión es exitosa, null en caso de error.
     */
    public static Connection getConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Conexión a la base de datos establecida con éxito.");
        } catch (SQLException e) {
            System.err.println("Error al establecer la conexión con la base de datos: " + e.getMessage());
            e.printStackTrace();
        }
        return connection;
    }

    /**
     * Cierra una conexión a la base de datos.
     * @param connection La conexión a cerrar.
     */
    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Conexión a la base de datos cerrada.");
            } catch (SQLException e) {
                System.err.println("Error al cerrar la conexión: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    // Método main para probar la conexión
    public static void main(String[] args) {
        Connection testConnection = DBConnection.getConnection();
        if (testConnection != null) {
            System.out.println("¡Prueba de conexión exitosa!");
            DBConnection.closeConnection(testConnection);
        } else {
            System.err.println("La prueba de conexión falló.");
        }
    }
}