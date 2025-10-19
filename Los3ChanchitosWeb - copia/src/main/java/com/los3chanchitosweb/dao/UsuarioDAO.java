package com.los3chanchitosweb.dao;

import com.los3chanchitosweb.model.Usuario;
import com.los3chanchitosweb.util.DBConnection; // Importa tu clase de conexión
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

// Importa la dependencia para hashing de contraseñas
import org.mindrot.jbcrypt.BCrypt; // Asegúrate de que esta dependencia está en tu pom.xml

public class UsuarioDAO {

    // Método para insertar un nuevo usuario en la base de datos
    public boolean insertarUsuario(Usuario usuario) {
        String SQL = "INSERT INTO usuarios (nombre, email, password, rol) VALUES (?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement pstmt = null;
        boolean exito = false;

        try {
            conn = DBConnection.getConnection();
            if (conn != null) {
                pstmt = conn.prepareStatement(SQL);
                pstmt.setString(1, usuario.getNombre());
                pstmt.setString(2, usuario.getEmail());
                // Hashea la contraseña antes de guardarla en la base de datos
                String hashedPassword = BCrypt.hashpw(usuario.getPassword(), BCrypt.gensalt());
                pstmt.setString(3, hashedPassword); // Guarda el hash, no la contraseña plana
                pstmt.setString(4, usuario.getRol());

                int filasAfectadas = pstmt.executeUpdate();
                exito = (filasAfectadas > 0);
            }
        } catch (SQLException e) {
            System.err.println("Error al insertar usuario: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DBConnection.closeConnection(conn); // Cierra la conexión usando tu utilidad
            try {
                if (pstmt != null) pstmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return exito;
    }

    // Método para buscar un usuario por email (útil para login)
    public Usuario buscarUsuarioPorEmail(String email) {
        String SQL = "SELECT id, nombre, email, password, rol FROM usuarios WHERE email like ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Usuario usuario = null;

        try {
            conn = DBConnection.getConnection();
            if (conn != null) {
                pstmt = conn.prepareStatement(SQL);
                pstmt.setString(1, email);
                rs = pstmt.executeQuery();

                if (rs.next()) {
                    usuario = new Usuario();
                    usuario.setId(rs.getInt("id"));
                    usuario.setNombre(rs.getString("nombre"));
                    usuario.setEmail(rs.getString("email"));
                    usuario.setPassword(rs.getString("password")); // Esto será el hash
                    usuario.setRol(rs.getString("rol"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar usuario por email: " + e.getMessage());
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
        return usuario;
    }

    // Método para verificar la contraseña de un usuario (para login)
    public boolean verificarContrasena(String email, String passwordPlana) {
        Usuario usuario = buscarUsuarioPorEmail(email);

        if (usuario != null) {
            // Compara la contraseña plana proporcionada con el hash almacenado
            System.out.println("verificarContrasena - method");
            return BCrypt.checkpw(passwordPlana, usuario.getPassword());
        }
        return false; // Usuario no encontrado
    }

    // Método para obtener todos los usuarios (ejemplo)
    public List<Usuario> obtenerTodosLosUsuarios() {
        List<Usuario> usuarios = new ArrayList<>();
        String SQL = "SELECT id, nombre, email, password, rol FROM usuarios";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            if (conn != null) {
                pstmt = conn.prepareStatement(SQL);
                rs = pstmt.executeQuery();

                while (rs.next()) {
                    Usuario usuario = new Usuario();
                    usuario.setId(rs.getInt("id"));
                    usuario.setNombre(rs.getString("nombre"));
                    usuario.setEmail(rs.getString("email"));
                    usuario.setPassword(rs.getString("password"));
                    usuario.setRol(rs.getString("rol"));
                    usuarios.add(usuario);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener todos los usuarios: " + e.getMessage());
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
        return usuarios;
    }

    // Agrega más métodos DAO según tus necesidades (actualizar, eliminar, etc.)

    // Método main para probar los métodos DAO
    public static void main(String[] args) {
        UsuarioDAO usuarioDAO = new UsuarioDAO();

        // --- Prueba de Inserción ---
        // La contraseña 'password123' se hasheará antes de guardarse
        Usuario nuevoUsuario1 = new Usuario("Juan Pérez", "juan@example.com", "password123", "cliente");
        if (usuarioDAO.insertarUsuario(nuevoUsuario1)) {
            System.out.println("Usuario Juan insertado con éxito.");
        } else {
            System.out.println("Fallo al insertar usuario Juan.");
        }

        Usuario nuevoUsuario2 = new Usuario("María García", "maria@example.com", "admin456", "admin");
        if (usuarioDAO.insertarUsuario(nuevoUsuario2)) {
            System.out.println("Usuario María insertado con éxito.");
        } else {
            System.out.println("Fallo al insertar usuario María.");
        }

        Usuario nuevoUsuario3 = new Usuario("Test User", "test@example.com", "password123", "cliente");
        if (usuarioDAO.insertarUsuario(nuevoUsuario3)) {
            System.out.println("Usuario Test User insertado con éxito.");
        } else {
            System.out.println("Fallo al insertar usuario Test User.");
        }


        System.out.println("\n--- Prueba de Búsqueda por Email y Verificación de Contraseña ---");
        // Prueba con un usuario que acabamos de insertar
        String emailABuscar1 = "juan@example.com";
        String contrasenaAProbar1 = "password123";
        Usuario encontrado1 = usuarioDAO.buscarUsuarioPorEmail(emailABuscar1);
        if (encontrado1 != null) {
            System.out.println("Usuario encontrado: " + encontrado1.getNombre());
            if (usuarioDAO.verificarContrasena(emailABuscar1, contrasenaAProbar1)) {
                System.out.println("Contraseña verificada correctamente para " + encontrado1.getNombre());
            } else {
                System.out.println("Contraseña incorrecta para " + encontrado1.getNombre());
            }
        } else {
            System.out.println("Usuario con email " + emailABuscar1 + " no encontrado.");
        }

        // Prueba con otro usuario
        String emailABuscar2 = "maria@example.com";
        String contrasenaAProbar2 = "admin456";
        Usuario encontrado2 = usuarioDAO.buscarUsuarioPorEmail(emailABuscar2);
        if (encontrado2 != null) {
            System.out.println("Usuario encontrado: " + encontrado2.getNombre());
            if (usuarioDAO.verificarContrasena(emailABuscar2, contrasenaAProbar2)) {
                System.out.println("Contraseña verificada correctamente para " + encontrado2.getNombre());
            } else {
                System.out.println("Contraseña incorrecta para " + encontrado2.getNombre());
            }
        } else {
            System.out.println("Usuario con email " + emailABuscar2 + " no encontrado.");
        }

        // Prueba con un usuario no existente
        String emailABuscar3 = "noexiste@example.com";
        if (usuarioDAO.buscarUsuarioPorEmail(emailABuscar3) == null) {
            System.out.println("Usuario con email " + emailABuscar3 + " correctamente no encontrado.");
        }


        System.out.println("\n--- Todos los Usuarios en la BD ---");
        List<Usuario> todosLosUsuarios = usuarioDAO.obtenerTodosLosUsuarios();
        if (todosLosUsuarios.isEmpty()) {
            System.out.println("No hay usuarios en la base de datos.");
        } else {
            for (Usuario u : todosLosUsuarios) {
                System.out.println(u);
            }
        }
    }
}