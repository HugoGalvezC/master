package com.los3chanchitosweb.dao;

import com.los3chanchitosweb.model.Pasaje;
import com.los3chanchitosweb.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PasajeDAO {

    public int insertarPasaje(Pasaje pasaje) throws SQLException {
        String sql = "INSERT INTO Pasajes (id_venta, id_viaje, id_pasajero, precio, numero_asiento, estado_pasaje) VALUES (?, ?, ?, ?, ?, ?)";
        int idGenerado = -1;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, pasaje.getIdVenta());
            pstmt.setInt(2, pasaje.getIdViaje());
            pstmt.setInt(3, pasaje.getIdPasajero());
            pstmt.setDouble(4, pasaje.getPrecio());
            // Si numeroAsiento puede ser null, usa setObject
            if (pasaje.getNumeroAsiento() != null) {
                pstmt.setInt(5, pasaje.getNumeroAsiento());
            } else {
                pstmt.setNull(5, Types.INTEGER); // Para insertar NULL
            }
            pstmt.setString(6, pasaje.getEstadoPasaje());

            int filasAfectadas = pstmt.executeUpdate();
            if (filasAfectadas > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    idGenerado = rs.getInt(1);
                }
            }
        }
        return idGenerado;
    }

    public List<Pasaje> obtenerPasajesPorIdVenta(int idVenta) throws SQLException {
        List<Pasaje> pasajes = new ArrayList<>();
        String sql = "SELECT id_pasaje, id_venta, id_viaje, id_pasajero, precio, numero_asiento, estado_pasaje FROM Pasajes WHERE id_venta = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idVenta);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Pasaje pasaje = new Pasaje(
                        rs.getInt("id_pasaje"),
                        rs.getInt("id_venta"),
                        rs.getInt("id_viaje"),
                        rs.getInt("id_pasajero"),
                        rs.getDouble("precio"),
                        (Integer) rs.getObject("numero_asiento"), // Esto ya lo tenías bien para manejar null
                        rs.getString("estado_pasaje")
                );
                pasajes.add(pasaje);
            }
        } catch (SQLException e) { // Añadir el catch aquí para re-lanzar
            System.err.println("Error al obtener pasajes por ID de venta: " + e.getMessage());
            throw e;
        }
        return pasajes;
    }

    // --- NUEVO MÉTODO PARA OBTENER UN SOLO PASAJE POR SU ID ---
    public Pasaje obtenerPasajePorId(int idPasaje) throws SQLException {
        Pasaje pasaje = null;
        String sql = "SELECT id_pasaje, id_venta, id_viaje, id_pasajero, precio, numero_asiento, estado_pasaje FROM Pasajes WHERE id_pasaje = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idPasaje);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) { // Usar if en lugar de while porque esperamos un solo resultado
                pasaje = new Pasaje(
                        rs.getInt("id_pasaje"),
                        rs.getInt("id_venta"),
                        rs.getInt("id_viaje"),
                        rs.getInt("id_pasajero"),
                        rs.getDouble("precio"),
                        (Integer) rs.getObject("numero_asiento"),
                        rs.getString("estado_pasaje")
                );
            }
        } catch (SQLException e) { // Añadir el catch aquí para re-lanzar
            System.err.println("Error al obtener pasaje por ID: " + e.getMessage());
            throw e;
        }
        return pasaje;
    }
    // -----------------------------------------------------------

    public List<Pasaje> obtenerPasajesPorIdViaje(int idViaje) throws SQLException {
        List<Pasaje> pasajes = new ArrayList<>();
        String sql = "SELECT id_pasaje, id_venta, id_viaje, id_pasajero, precio, numero_asiento, estado_pasaje FROM Pasajes WHERE id_viaje = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idViaje);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Pasaje pasaje = new Pasaje(
                        rs.getInt("id_pasaje"),
                        rs.getInt("id_venta"),
                        rs.getInt("id_viaje"),
                        rs.getInt("id_pasajero"),
                        rs.getDouble("precio"),
                        (Integer) rs.getObject("numero_asiento"), // Puede ser null
                        rs.getString("estado_pasaje")
                );
                pasajes.add(pasaje);
            }
        } catch (SQLException e) { // Añadir el catch aquí para re-lanzar
            System.err.println("Error al obtener pasajes por ID de viaje: " + e.getMessage());
            throw e;
        }
        return pasajes;
    }
}