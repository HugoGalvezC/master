package com.los3chanchitosweb.dao;

import com.los3chanchitosweb.model.Pasajero;
import com.los3chanchitosweb.util.DBConnection; // Asegúrate de que esta clase exista

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PasajeroDAO {

    public int insertarPasajero(Pasajero pasajero) throws SQLException {
        String sql = "INSERT INTO Pasajeros (nombre, apellido, dni, email, telefono) VALUES (?, ?, ?, ?, ?)";
        int idGenerado = -1; // Para almacenar el ID autoincrementable

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, pasajero.getNombre());
            pstmt.setString(2, pasajero.getApellido());
            pstmt.setString(3, pasajero.getDni());
            pstmt.setString(4, pasajero.getEmail());
            pstmt.setString(5, pasajero.getTelefono());

            int filasAfectadas = pstmt.executeUpdate();
            if (filasAfectadas > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    idGenerado = rs.getInt(1); // Recupera el ID generado
                }
            }
        }
        return idGenerado; // Retorna el ID del pasajero insertado
    }

    public Pasajero buscarPasajeroPorDNI(String dni) throws SQLException {
        String sql = "SELECT id_pasajero, nombre, apellido, dni, email, telefono FROM Pasajeros WHERE dni = ?";
        Pasajero pasajero = null;
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, dni);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                pasajero = new Pasajero(
                        rs.getInt("id_pasajero"),
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getString("dni"),
                        rs.getString("email"),
                        rs.getString("telefono")
                );
            }
        }
        return pasajero;
    }

    // Puedes agregar más métodos CRUD (obtener todos, actualizar, eliminar) según sea necesario
    public Pasajero obtenerPasajeroPorId(int id) throws SQLException {
        String sql = "SELECT id_pasajero, nombre, apellido, dni, email, telefono FROM Pasajeros WHERE id_pasajero = ?";
        Pasajero pasajero = null;
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                pasajero = new Pasajero(
                        rs.getInt("id_pasajero"),
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getString("dni"),
                        rs.getString("email"),
                        rs.getString("telefono")
                );
            }
        }
        return pasajero;
    }
}