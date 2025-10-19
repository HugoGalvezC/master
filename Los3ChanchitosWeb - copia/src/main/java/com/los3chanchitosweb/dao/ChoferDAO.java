package com.los3chanchitosweb.dao;

import com.los3chanchitosweb.model.Chofer;
import com.los3chanchitosweb.util.DBConnection; // Asegúrate de que esta importación sea correcta para tu clase de conexión
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement; // Importar Statement para obtenerTodosLosChoferes
import java.util.ArrayList;
import java.util.List;

public class ChoferDAO {

    /**
     * Obtiene una lista de todos los choferes registrados en la base de datos.
     * @return Una lista de objetos Chofer.
     * @throws SQLException si ocurre un error en la base de datos.
     */
    public List<Chofer> obtenerTodosLosChoferes() throws SQLException {
        List<Chofer> choferes = new ArrayList<>();
        String SQL = "SELECT id_chofer, nombre, licencia, horas_conduccion_acumuladas FROM choferes";
        Connection conn = null;
        Statement stmt = null; // Usar Statement para consultas sin parámetros
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(SQL);

            while (rs.next()) {
                Chofer chofer = new Chofer(
                        rs.getInt("id_chofer"),
                        rs.getString("nombre"),
                        rs.getString("licencia"),
                        rs.getDouble("horas_conduccion_acumuladas")
                );
                choferes.add(chofer);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener todos los choferes: " + e.getMessage());
            e.printStackTrace();
            throw e; // Re-lanzar la excepción
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close(); // Cerrar Statement
                if (conn != null) DBConnection.closeConnection(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return choferes;
    }

    /**
     * Obtiene un chofer por su ID.
     * @param idChofer El ID del chofer a obtener.
     * @return El objeto Chofer si se encuentra, o null si no existe.
     * @throws SQLException si ocurre un error en la base de datos.
     */
    public Chofer obtenerChoferPorId(int idChofer) throws SQLException {
        Chofer chofer = null;
        String SQL = "SELECT id_chofer, nombre, licencia, horas_conduccion_acumuladas FROM choferes WHERE id_chofer = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(SQL);
            pstmt.setInt(1, idChofer);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                chofer = new Chofer(
                        rs.getInt("id_chofer"),
                        rs.getString("nombre"),
                        rs.getString("licencia"),
                        rs.getDouble("horas_conduccion_acumuladas")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener chofer por ID " + idChofer + ": " + e.getMessage());
            e.printStackTrace();
            throw e; // Re-lanzar la excepción
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) DBConnection.closeConnection(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return chofer;
    }

    /**
     * Actualiza un chofer existente en la base de datos.
     * @param chofer El objeto Chofer con los datos actualizados.
     * @return true si el chofer fue actualizado exitosamente, false en caso contrario.
     * @throws SQLException si ocurre un error en la base de datos.
     */
    public boolean actualizarChofer(Chofer chofer) throws SQLException {
        String SQL = "UPDATE choferes SET nombre = ?, licencia = ?, horas_conduccion_acumuladas = ? WHERE id_chofer = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        boolean exito = false;

        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(SQL);
            pstmt.setString(1, chofer.getNombre());
            pstmt.setString(2, chofer.getLicencia());
            pstmt.setDouble(3, chofer.getHorasConduccionAcumuladas());
            pstmt.setInt(4, chofer.getIdChofer());

            int filasAfectadas = pstmt.executeUpdate();
            exito = (filasAfectadas > 0);
        } catch (SQLException e) {
            System.err.println("Error al actualizar chofer con ID " + chofer.getIdChofer() + ": " + e.getMessage());
            e.printStackTrace();
            throw e; // Re-lanzar la excepción
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                if (conn != null) DBConnection.closeConnection(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return exito;
    }
}