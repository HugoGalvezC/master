package com.los3chanchitosweb.dao;

import com.los3chanchitosweb.model.Venta;
import com.los3chanchitosweb.util.DBConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class VentaDAO {

    public int insertarVenta(Venta venta) throws SQLException {
        String sql = "INSERT INTO Ventas (fecha_venta, id_vendedor, monto_total, estado_venta) VALUES (?, ?, ?, ?)";
        int idGenerado = -1;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setTimestamp(1, Timestamp.valueOf(venta.getFechaVenta()));
            pstmt.setInt(2, venta.getIdVendedor());
            pstmt.setDouble(3, venta.getMontoTotal());
            pstmt.setString(4, venta.getEstadoVenta());

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

    public Venta obtenerVentaPorId(int idVenta) throws SQLException {
        String sql = "SELECT id_venta, fecha_venta, id_vendedor, monto_total, estado_venta FROM Ventas WHERE id_venta = ?";
        Venta venta = null;
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idVenta);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                venta = new Venta(
                        rs.getInt("id_venta"),
                        rs.getTimestamp("fecha_venta").toLocalDateTime(),
                        rs.getInt("id_vendedor"),
                        rs.getDouble("monto_total"),
                        rs.getString("estado_venta")
                );
            }
        }
        return venta;
    }

    /**
     * Anula una venta y libera el asiento ocupado en el viaje correspondiente.
     * Esta operación se realiza dentro de una transacción para asegurar la integridad de los datos.
     * @param idVenta El ID de la venta a anular.
     * @return true si la anulación fue exitosa, false en caso contrario.
     * @throws SQLException si ocurre un error en la base de datos.
     */
    public boolean anularVenta(int idVenta) throws SQLException {
        boolean anulado = false;
        String sqlAnularVenta = "UPDATE ventas SET estado_venta = 'Anulada' WHERE id_venta = ?;";
        String sqlSelectIdViaje = "SELECT id_viaje FROM pasajes WHERE id_venta = ? LIMIT 1;";
        String sqlIncrementarAsientos = "UPDATE viajes SET asientos_disponibles = asientos_disponibles + 1 WHERE id_viaje = ?;";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement psAnular = conn.prepareStatement(sqlAnularVenta);
             PreparedStatement psSelectViaje = conn.prepareStatement(sqlSelectIdViaje);
             PreparedStatement psIncrementar = conn.prepareStatement(sqlIncrementarAsientos)) {

            conn.setAutoCommit(false); // Iniciar la transacción

            // 1. Anular la venta
            psAnular.setInt(1, idVenta);
            int filasAnuladas = psAnular.executeUpdate();

            // 2. Si la venta fue anulada, buscar el viaje y liberar el asiento
            if (filasAnuladas > 0) {
                psSelectViaje.setInt(1, idVenta);
                try (ResultSet rs = psSelectViaje.executeQuery()) {
                    if (rs.next()) {
                        int idViaje = rs.getInt("id_viaje");

                        // 3. Incrementar los asientos disponibles
                        psIncrementar.setInt(1, idViaje);
                        int filasIncrementadas = psIncrementar.executeUpdate();

                        if (filasIncrementadas > 0) {
                            conn.commit(); // Confirmar la transacción
                            anulado = true;
                        } else {
                            conn.rollback(); // Deshacer si no se pudo actualizar el asiento
                        }
                    } else {
                        conn.rollback();
                    }
                }
            } else {
                conn.rollback();
            }
        } catch (SQLException e) {
            try (Connection conn = DBConnection.getConnection()) {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            throw e;
        }
        return anulado;
    }

    // Método para obtener todas las ventas (útil para reportes)
    public List<Venta> obtenerTodasLasVentas() throws SQLException {
        List<Venta> ventas = new ArrayList<>();
        String sql = "SELECT id_venta, fecha_venta, id_vendedor, monto_total, estado_venta FROM Ventas ORDER BY fecha_venta DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Venta venta = new Venta(
                        rs.getInt("id_venta"),
                        rs.getTimestamp("fecha_venta").toLocalDateTime(),
                        rs.getInt("id_vendedor"),
                        rs.getDouble("monto_total"),
                        rs.getString("estado_venta")
                );
                ventas.add(venta);
            }
        }
        return ventas;
    }
}