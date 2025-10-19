package com.los3chanchitosweb.dao;

import com.los3chanchitosweb.model.Bus;
import com.los3chanchitosweb.util.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BusDAO {

    public List<Bus> obtenerTodosLosBuses() {
        List<Bus> buses = new ArrayList<>();
        // Asegúrate de que 'capacidad_asientos' sea el nombre correcto de tu columna en la DB
        String SQL = "SELECT id_bus, patente, modelo, capacidad_asientos FROM buses";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            if (conn != null) {
                pstmt = conn.prepareStatement(SQL);
                rs = pstmt.executeQuery();

                while (rs.next()) {
                    // Usando el constructor con parámetros si está disponible, o setters
                    buses.add(new Bus(
                            rs.getInt("id_bus"),
                            rs.getString("patente"),
                            rs.getString("modelo"),
                            rs.getInt("capacidad_asientos") // Asegúrate de que el nombre de la columna sea correcto
                    ));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener todos los buses: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DBConnection.closeConnection(conn);
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return buses;
    }

    public Bus obtenerBusPorId(int idBus) {
        String SQL = "SELECT id_bus, patente, modelo, capacidad_asientos FROM buses WHERE id_bus = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Bus bus = null;

        try {
            conn = DBConnection.getConnection();
            if (conn != null) {
                pstmt = conn.prepareStatement(SQL);
                pstmt.setInt(1, idBus);
                rs = pstmt.executeQuery();

                if (rs.next()) {
                    // Usando el constructor con parámetros si está disponible, o setters
                    bus = new Bus(
                            rs.getInt("id_bus"),
                            rs.getString("patente"),
                            rs.getString("modelo"),
                            rs.getInt("capacidad_asientos") // Asegúrate de que el nombre de la columna sea correcto
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener bus por ID: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DBConnection.closeConnection(conn);
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return bus;
    }

    // Puedes añadir métodos para insertar, actualizar, eliminar buses si es necesario.
}