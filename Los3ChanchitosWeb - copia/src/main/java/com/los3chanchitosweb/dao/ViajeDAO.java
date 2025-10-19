package com.los3chanchitosweb.dao;

import com.los3chanchitosweb.model.Viaje;
import com.los3chanchitosweb.model.Bus;
import com.los3chanchitosweb.model.Chofer;
import com.los3chanchitosweb.model.Auxiliar;
import com.los3chanchitosweb.util.DBConnection; // Asegúrate de que esta clase exista y sea funcional

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ViajeDAO {

    /**
     * Inserta un nuevo viaje en la base de datos.
     * Utiliza try-with-resources para asegurar el cierre automático de recursos.
     *
     * @param viaje El objeto Viaje a insertar.
     * @return true si el viaje fue insertado exitosamente, false en caso contrario.
     * @throws SQLException Si ocurre un error de base de datos.
     */
    public boolean insertarViaje(Viaje viaje) throws SQLException {
        String SQL = "INSERT INTO viajes (origen, destino, fecha_hora_salida, id_bus, id_chofer, id_auxiliar, asientos_disponibles, estado_viaje) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        boolean exito = false;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {

            pstmt.setString(1, viaje.getOrigen());
            pstmt.setString(2, viaje.getDestino());
            pstmt.setTimestamp(3, Timestamp.valueOf(viaje.getFechaHoraSalida()));
            pstmt.setInt(4, viaje.getIdBus());
            pstmt.setInt(5, viaje.getIdChofer());
            if (viaje.getIdAuxiliar() != null) {
                pstmt.setInt(6, viaje.getIdAuxiliar());
            } else {
                pstmt.setNull(6, java.sql.Types.INTEGER);
            }
            pstmt.setInt(7, viaje.getAsientosDisponibles());
            pstmt.setString(8, viaje.getEstadoViaje());

            int filasAfectadas = pstmt.executeUpdate();
            exito = (filasAfectadas > 0);
        } catch (SQLException e) {
            System.err.println("Error al insertar viaje: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
        return exito;
    }

    /**
     * Obtiene una lista de todos los viajes programados, incluyendo detalles de Bus, Chofer y Auxiliar.
     * Utiliza try-with-resources para asegurar el cierre automático de recursos.
     *
     * @return Una lista de objetos Viaje.
     * @throws SQLException Si ocurre un error de base de datos.
     */
    public List<Viaje> obtenerTodosLosViajes() throws SQLException {
        List<Viaje> viajes = new ArrayList<>();
        String SQL = "SELECT v.id_viaje, v.origen, v.destino, v.fecha_hora_salida, v.estado_viaje, v.asientos_disponibles, " +
                "b.id_bus, b.patente, b.modelo, b.capacidad_asientos, " +
                "c.id_chofer, c.nombre AS chofer_nombre, c.licencia, c.horas_conduccion_acumuladas, " +
                "a.id_auxiliar, a.nombre AS auxiliar_nombre, " +
                // -- START MODIFIED SECTION - Add attendance columns to SELECT --
                "v.estado_asistencia_chofer, v.fecha_hora_asistencia_chofer " +
                // -- END MODIFIED SECTION --
                "FROM viajes v " +
                "JOIN buses b ON v.id_bus = b.id_bus " +
                "JOIN choferes c ON v.id_chofer = c.id_chofer " +
                "LEFT JOIN auxiliares a ON v.id_auxiliar = a.id_auxiliar " +
                "ORDER BY v.fecha_hora_salida ASC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Bus bus = new Bus(
                        rs.getInt("id_bus"),
                        rs.getString("patente"),
                        rs.getString("modelo"),
                        rs.getInt("capacidad_asientos")
                );

                Chofer chofer = new Chofer(
                        rs.getInt("id_chofer"),
                        rs.getString("chofer_nombre"),
                        rs.getString("licencia"),
                        rs.getDouble("horas_conduccion_acumuladas")
                );

                Auxiliar auxiliar = null;
                int idAux = rs.getInt("id_auxiliar");
                if (!rs.wasNull()) { // Correcto uso de rs.wasNull()
                    auxiliar = new Auxiliar(
                            idAux,
                            rs.getString("auxiliar_nombre")
                    );
                }

                // -- START MODIFIED SECTION - Update Viaje constructor call to include new fields --
                // Obtener los valores de asistencia del ResultSet
                String estadoAsistenciaChofer = rs.getString("estado_asistencia_chofer");
                Timestamp tsAsistencia = rs.getTimestamp("fecha_hora_asistencia_chofer");
                LocalDateTime fechaHoraAsistenciaChofer = (tsAsistencia != null) ? tsAsistencia.toLocalDateTime() : null;

                Viaje viaje = new Viaje(
                        rs.getInt("id_viaje"),
                        rs.getString("origen"),
                        rs.getString("destino"),
                        rs.getTimestamp("fecha_hora_salida").toLocalDateTime(),
                        rs.getInt("asientos_disponibles"),
                        bus,
                        chofer,
                        auxiliar,
                        rs.getString("estado_viaje"),
                        estadoAsistenciaChofer, // Pasar el nuevo campo
                        fechaHoraAsistenciaChofer // Pasar el nuevo campo
                );
                // -- END MODIFIED SECTION --

                viajes.add(viaje);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener todos los viajes: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
        return viajes;
    }

    /**
     * Elimina un viaje de la base de datos por su ID.
     * Utiliza try-with-resources para asegurar el cierre automático de recursos.
     *
     * @param idViaje El ID del viaje a eliminar.
     * @return true si el viaje fue eliminado exitosamente, false en caso contrario.
     * @throws SQLException Si ocurre un error de base de datos.
     */
    public boolean eliminarViaje(int idViaje) throws SQLException {
        String SQL = "DELETE FROM viajes WHERE id_viaje = ?";
        boolean exito = false;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {

            pstmt.setInt(1, idViaje);

            int filasAfectadas = pstmt.executeUpdate();
            exito = (filasAfectadas > 0);
        } catch (SQLException e) {
            System.err.println("Error al eliminar viaje con ID " + idViaje + ": " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
        return exito;
    }

    /**
     * Obtiene un viaje específico de la base de datos por su ID.
     * Utiliza try-with-resources para asegurar el cierre automático de recursos.
     *
     * @param idViaje El ID del viaje a obtener.
     * @return El objeto Viaje si se encuentra, o null si no existe.
     * @throws SQLException Si ocurre un error de base de datos.
     */
    public Viaje obtenerViajePorId(int idViaje) throws SQLException {
        Viaje viaje = null;
        String SQL = "SELECT v.id_viaje, v.origen, v.destino, v.fecha_hora_salida, v.estado_viaje, v.asientos_disponibles, " +
                "b.id_bus, b.patente, b.modelo, b.capacidad_asientos, " +
                "c.id_chofer, c.nombre AS chofer_nombre, c.licencia, c.horas_conduccion_acumuladas, " +
                "a.id_auxiliar, a.nombre AS auxiliar_nombre, " +
                // -- START MODIFIED SECTION - Add attendance columns to SELECT --
                "v.estado_asistencia_chofer, v.fecha_hora_asistencia_chofer " +
                // -- END MODIFIED SECTION --
                "FROM viajes v " +
                "JOIN buses b ON v.id_bus = b.id_bus " +
                "JOIN choferes c ON v.id_chofer = c.id_chofer " +
                "LEFT JOIN auxiliares a ON v.id_auxiliar = a.id_auxiliar " +
                "WHERE v.id_viaje = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL);) {

            pstmt.setInt(1, idViaje);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Bus bus = new Bus(
                            rs.getInt("id_bus"),
                            rs.getString("patente"),
                            rs.getString("modelo"),
                            rs.getInt("capacidad_asientos")
                    );

                    Chofer chofer = new Chofer(
                            rs.getInt("id_chofer"),
                            rs.getString("chofer_nombre"),
                            rs.getString("licencia"),
                            rs.getDouble("horas_conduccion_acumuladas")
                    );

                    Auxiliar auxiliar = null;
                    int idAux = rs.getInt("id_auxiliar");
                    if (!rs.wasNull()) { // Correcto
                        auxiliar = new Auxiliar(
                                idAux,
                                rs.getString("auxiliar_nombre")
                        );
                    }

                    // -- START MODIFIED SECTION - Update Viaje constructor call to include new fields --
                    // Obtener los valores de asistencia del ResultSet
                    String estadoAsistenciaChofer = rs.getString("estado_asistencia_chofer");
                    Timestamp tsAsistencia = rs.getTimestamp("fecha_hora_asistencia_chofer");
                    LocalDateTime fechaHoraAsistenciaChofer = (tsAsistencia != null) ? tsAsistencia.toLocalDateTime() : null;

                    viaje = new Viaje(
                            rs.getInt("id_viaje"),
                            rs.getString("origen"),
                            rs.getString("destino"),
                            rs.getTimestamp("fecha_hora_salida").toLocalDateTime(),
                            rs.getInt("asientos_disponibles"),
                            bus,
                            chofer,
                            auxiliar,
                            rs.getString("estado_viaje"),
                            estadoAsistenciaChofer, // Pasar el nuevo campo
                            fechaHoraAsistenciaChofer // Pasar el nuevo campo
                    );
                    // -- END MODIFIED SECTION --
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener viaje por ID " + idViaje + ": " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
        return viaje;
    }

    /**
     * Actualiza un viaje existente en la base de datos.
     * Utiliza try-with-resources para asegurar el cierre automático de recursos.
     *
     * @param viaje El objeto Viaje con los datos actualizados.
     * @return true si el viaje fue actualizado exitosamente, false en caso contrario.
     * @throws SQLException Si ocurre un error de base de datos.
     */
    public boolean actualizarViaje(Viaje viaje) throws SQLException {
        String SQL = "UPDATE viajes SET origen = ?, destino = ?, fecha_hora_salida = ?, " +
                "id_bus = ?, id_chofer = ?, id_auxiliar = ?, " +
                "asientos_disponibles = ?, estado_viaje = ?, " +
                // -- START MODIFIED SECTION - Add attendance columns to UPDATE --
                "estado_asistencia_chofer = ?, fecha_hora_asistencia_chofer = ? " +
                // -- END MODIFIED SECTION --
                "WHERE id_viaje = ?";
        boolean exito = false;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {

            pstmt.setString(1, viaje.getOrigen());
            pstmt.setString(2, viaje.getDestino());
            pstmt.setTimestamp(3, Timestamp.valueOf(viaje.getFechaHoraSalida()));
            pstmt.setInt(4, viaje.getIdBus());
            pstmt.setInt(5, viaje.getIdChofer());
            if (viaje.getIdAuxiliar() != null) {
                pstmt.setInt(6, viaje.getIdAuxiliar());
            } else {
                pstmt.setNull(6, java.sql.Types.INTEGER);
            }
            pstmt.setInt(7, viaje.getAsientosDisponibles());
            pstmt.setString(8, viaje.getEstadoViaje());

            // -- START MODIFIED SECTION - Set attendance parameters for UPDATE --
            pstmt.setString(9, viaje.getEstadoAsistenciaChofer());
            if (viaje.getFechaHoraAsistenciaChofer() != null) {
                pstmt.setTimestamp(10, Timestamp.valueOf(viaje.getFechaHoraAsistenciaChofer()));
            } else {
                pstmt.setNull(10, java.sql.Types.TIMESTAMP);
            }
            // -- END MODIFIED SECTION --

            pstmt.setInt(11, viaje.getIdViaje()); // Condición WHERE (el índice cambió por las nuevas columnas)

            int filasAfectadas = pstmt.executeUpdate();
            exito = (filasAfectadas > 0);
        } catch (SQLException e) {
            System.err.println("Error al actualizar viaje con ID " + viaje.getIdViaje() + ": " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
        return exito;
    }

    /**
     * Obtiene el próximo viaje asignado a un chofer específico.
     * Considera viajes en el futuro, ordenados por fecha y hora de salida.
     * Utiliza try-with-resources para asegurar el cierre automático de recursos.
     *
     * @param idChofer El ID del chofer.
     * @return El objeto Viaje del próximo viaje asignado, o null si no hay ninguno.
     * @throws SQLException Si ocurre un error de base de datos.
     */
    public Viaje obtenerProximoViajeAsignadoAChofer(int idChofer) throws SQLException {
        Viaje viaje = null;
        String sql = "SELECT v.id_viaje, v.origen, v.destino, v.fecha_hora_salida, v.id_bus, v.id_chofer, v.id_auxiliar, v.estado_viaje, v.asientos_disponibles, " +
                // -- START MODIFIED SECTION - Add new attendance columns to the SELECT query --
                "v.estado_asistencia_chofer, v.fecha_hora_asistencia_chofer, " +
                // -- END MODIFIED SECTION --
                "b.patente, b.modelo, b.capacidad_asientos, " +
                "c.nombre AS chofer_nombre, c.licencia AS chofer_licencia, c.horas_conduccion_acumuladas AS chofer_horas_conduccion_acumuladas, " +
                "a.nombre AS auxiliar_nombre " +
                "FROM viajes v " +
                "JOIN buses b ON v.id_bus = b.id_bus " +
                "JOIN choferes c ON v.id_chofer = c.id_chofer " +
                "LEFT JOIN auxiliares a ON v.id_auxiliar = a.id_auxiliar " +
                "WHERE v.id_chofer = ? " +
                "AND v.fecha_hora_salida > NOW() " +
                "ORDER BY v.fecha_hora_salida ASC " +
                "LIMIT 1";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idChofer);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    viaje = new Viaje();
                    viaje.setIdViaje(rs.getInt("id_viaje"));
                    viaje.setOrigen(rs.getString("origen"));
                    viaje.setDestino(rs.getString("destino"));

                    Timestamp ts = rs.getTimestamp("fecha_hora_salida");
                    if (ts != null) {
                        viaje.setFechaHoraSalida(ts.toLocalDateTime());
                    }

                    // Cargar datos del Bus
                    Bus bus = new Bus();
                    bus.setIdBus(rs.getInt("id_bus"));
                    bus.setPatente(rs.getString("patente"));
                    bus.setModelo(rs.getString("modelo"));
                    bus.setCapacidadAsientos(rs.getInt("capacidad_asientos"));
                    viaje.setBus(bus);

                    // Cargar datos del Chofer
                    Chofer chofer = new Chofer();
                    chofer.setIdChofer(rs.getInt("id_chofer"));
                    chofer.setNombre(rs.getString("chofer_nombre"));
                    chofer.setLicencia(rs.getString("chofer_licencia"));
                    chofer.setHorasConduccionAcumuladas(rs.getDouble("chofer_horas_conduccion_acumuladas"));
                    viaje.setChofer(chofer);

                    // Cargar datos del Auxiliar (si existe)
                    int idAuxiliar = rs.getInt("id_auxiliar");
                    if (!rs.wasNull()) { // Correcto
                        Auxiliar auxiliar = new Auxiliar();
                        auxiliar.setIdAuxiliar(idAuxiliar);
                        auxiliar.setNombre(rs.getString("auxiliar_nombre"));
                        viaje.setAuxiliar(auxiliar);
                    }

                    viaje.setEstadoViaje(rs.getString("estado_viaje"));
                    viaje.setAsientosDisponibles(rs.getInt("asientos_disponibles"));

                    // -- START ADDED/MODIFIED SECTION - Load new attendance fields --
                    viaje.setEstadoAsistenciaChofer(rs.getString("estado_asistencia_chofer"));
                    Timestamp tsAsistencia = rs.getTimestamp("fecha_hora_asistencia_chofer");
                    if (tsAsistencia != null) {
                        viaje.setFechaHoraAsistenciaChofer(tsAsistencia.toLocalDateTime());
                    } else {
                        viaje.setFechaHoraAsistenciaChofer(null);
                    }
                    // -- END ADDED/MODIFIED SECTION --
                }
            }
        } catch (SQLException e) {
            System.err.println("ERROR: SQLException al obtener próximo viaje para chofer " + idChofer + ": " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
        return viaje;
    }

    // -- START ADDED SECTION - New method for marking driver attendance --
    /**
     * Marca la asistencia del chofer para un viaje específico.
     * Actualiza el estado de asistencia y la fecha/hora en la base de datos.
     * @param idViaje El ID del viaje cuya asistencia se va a marcar.
     * @return true si la asistencia se marcó con éxito, false en caso contrario.
     * @throws SQLException Si ocurre un error de base de datos.
     */
    public boolean marcarAsistenciaChofer(int idViaje) throws SQLException {
        String SQL = "UPDATE viajes SET estado_asistencia_chofer = ?, fecha_hora_asistencia_chofer = ? WHERE id_viaje = ?";
        boolean exito = false;
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            pstmt.setString(1, "Asistió"); // Establece el estado a 'Asistió'
            pstmt.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now())); // Registra la hora actual
            pstmt.setInt(3, idViaje);

            int filasAfectadas = pstmt.executeUpdate();
            exito = (filasAfectadas > 0); // Si al menos una fila fue afectada, fue un éxito
        } catch (SQLException e) {
            System.err.println("Error al marcar asistencia del chofer para viaje " + idViaje + ": " + e.getMessage());
            e.printStackTrace();
            throw e; // Re-lanza la excepción para que el Servlet la maneje
        }
        return exito;
    }
    // -- END ADDED SECTION --
}