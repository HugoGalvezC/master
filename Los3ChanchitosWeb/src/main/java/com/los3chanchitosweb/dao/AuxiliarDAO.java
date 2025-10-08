package com.los3chanchitosweb.dao;

import com.los3chanchitosweb.model.Auxiliar;
import com.los3chanchitosweb.util.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AuxiliarDAO {

    public List<Auxiliar> obtenerTodosLosAuxiliares() {
        List<Auxiliar> auxiliares = new ArrayList<>();
        String SQL = "SELECT id_auxiliar, nombre FROM auxiliares"; // Ajusta si tienes más columnas como apellido
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
                    auxiliares.add(new Auxiliar(
                            rs.getInt("id_auxiliar"),
                            rs.getString("nombre")
                    ));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener todos los auxiliares: " + e.getMessage());
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
        return auxiliares;
    }

    public Auxiliar obtenerAuxiliarPorId(int idAuxiliar) {
        String SQL = "SELECT id_auxiliar, nombre FROM auxiliares WHERE id_auxiliar = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Auxiliar auxiliar = null;

        try {
            conn = DBConnection.getConnection();
            if (conn != null) {
                pstmt = conn.prepareStatement(SQL);
                pstmt.setInt(1, idAuxiliar);
                rs = pstmt.executeQuery();

                if (rs.next()) {
                    // Usando el constructor con parámetros si está disponible, o setters
                    auxiliar = new Auxiliar(
                            rs.getInt("id_auxiliar"),
                            rs.getString("nombre")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener auxiliar por ID: " + e.getMessage());
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
        return auxiliar;
    }

    // Puedes añadir métodos para insertar, actualizar, eliminar auxiliares.
}