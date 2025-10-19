package com.los3chanchitosweb.dao;

import com.los3chanchitosweb.model.Reserva;
import com.los3chanchitosweb.util.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ReservaDAO {

    /**
     * Inserta una nueva reserva en la base de datos.
     * @param reserva El objeto Reserva a insertar.
     * @return true si la reserva fue insertada exitosamente, false en caso contrario.
     */
    public boolean insertarReserva(Reserva reserva) {
        String SQL = "INSERT INTO reservas (id_usuario, id_viaje, cantidad_asientos, fecha_reserva, estado_reserva) VALUES (?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement pstmt = null;
        boolean exito = false;

        try {
            conn = DBConnection.getConnection();
            if (conn != null) {
                pstmt = conn.prepareStatement(SQL);
                pstmt.setInt(1, reserva.getIdUsuario());
                pstmt.setInt(2, reserva.getIdViaje());
                pstmt.setInt(3, reserva.getCantidadAsientos());
                pstmt.setTimestamp(4, Timestamp.valueOf(reserva.getFechaReserva()));
                pstmt.setString(5, reserva.getEstadoReserva());

                int filasAfectadas = pstmt.executeUpdate();
                exito = (filasAfectadas > 0);
            }
        } catch (SQLException e) {
            System.err.println("Error al insertar reserva: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DBConnection.closeConnection(conn);
            try {
                if (pstmt != null) pstmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return exito;
    }

    /**
     * Obtiene una reserva por su ID.
     * @param idReserva El ID de la reserva a buscar.
     * @return El objeto Reserva si se encuentra, null en caso contrario.
     */
    public Reserva obtenerReservaPorId(int idReserva) {
        String SQL = "SELECT id_reserva, id_usuario, id_viaje, cantidad_asientos, fecha_reserva, estado_reserva FROM reservas WHERE id_reserva = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Reserva reserva = null;

        try {
            conn = DBConnection.getConnection();
            if (conn != null) {
                pstmt = conn.prepareStatement(SQL);
                pstmt.setInt(1, idReserva);
                rs = pstmt.executeQuery();

                if (rs.next()) {
                    reserva = new Reserva(
                            rs.getInt("id_reserva"),
                            rs.getInt("id_usuario"),
                            rs.getInt("id_viaje"),
                            rs.getInt("cantidad_asientos"),
                            rs.getTimestamp("fecha_reserva").toLocalDateTime(),
                            rs.getString("estado_reserva")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener reserva por ID: " + e.getMessage());
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
        return reserva;
    }

    /**
     * Obtiene todas las reservas.
     * @return Una lista de objetos Reserva.
     */
    public List<Reserva> obtenerTodasLasReservas() {
        List<Reserva> reservas = new ArrayList<>();
        String SQL = "SELECT id_reserva, id_usuario, id_viaje, cantidad_asientos, fecha_reserva, estado_reserva FROM reservas ORDER BY fecha_reserva DESC";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            if (conn != null) {
                pstmt = conn.prepareStatement(SQL);
                rs = pstmt.executeQuery();

                while (rs.next()) {
                    reservas.add(new Reserva(
                            rs.getInt("id_reserva"),
                            rs.getInt("id_usuario"),
                            rs.getInt("id_viaje"),
                            rs.getInt("cantidad_asientos"),
                            rs.getTimestamp("fecha_reserva").toLocalDateTime(),
                            rs.getString("estado_reserva")
                    ));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener todas las reservas: " + e.getMessage());
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
        return reservas;
    }

    /**
     * Actualiza una reserva existente en la base de datos.
     * @param reserva El objeto Reserva con los datos actualizados.
     * @return true si la reserva fue actualizada exitosamente, false en caso contrario.
     */
    public boolean actualizarReserva(Reserva reserva) {
        String SQL = "UPDATE reservas SET id_usuario = ?, id_viaje = ?, cantidad_asientos = ?, fecha_reserva = ?, estado_reserva = ? WHERE id_reserva = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        boolean exito = false;

        try {
            conn = DBConnection.getConnection();
            if (conn != null) {
                pstmt = conn.prepareStatement(SQL);
                pstmt.setInt(1, reserva.getIdUsuario());
                pstmt.setInt(2, reserva.getIdViaje());
                pstmt.setInt(3, reserva.getCantidadAsientos());
                pstmt.setTimestamp(4, Timestamp.valueOf(reserva.getFechaReserva()));
                pstmt.setString(5, reserva.getEstadoReserva());
                pstmt.setInt(6, reserva.getIdReserva());

                int filasAfectadas = pstmt.executeUpdate();
                exito = (filasAfectadas > 0);
            }
        } catch (SQLException e) {
            System.err.println("Error al actualizar reserva: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DBConnection.closeConnection(conn);
            try {
                if (pstmt != null) pstmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return exito;
    }

    /**
     * Elimina una reserva de la base de datos por su ID.
     * @param idReserva El ID de la reserva a eliminar.
     * @return true si la reserva fue eliminada exitosamente, false en caso contrario.
     */
    public boolean eliminarReserva(int idReserva) {
        String SQL = "DELETE FROM reservas WHERE id_reserva = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        boolean exito = false;

        try {
            conn = DBConnection.getConnection();
            if (conn != null) {
                pstmt = conn.prepareStatement(SQL);
                pstmt.setInt(1, idReserva);

                int filasAfectadas = pstmt.executeUpdate();
                exito = (filasAfectadas > 0);
            }
        } catch (SQLException e) {
            System.err.println("Error al eliminar reserva: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DBConnection.closeConnection(conn);
            try {
                if (pstmt != null) pstmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return exito;
    }
}