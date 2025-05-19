package com.transporte.service;

import com.transporte.model.Usuario;
import java.util.List;

public interface UsuarioService {
    Usuario registrarUsuario(Usuario usuario);
    Usuario actualizarUsuario(Usuario usuario);
    void eliminarUsuario(Long id);
    Usuario obtenerUsuarioPorId(Long id);
    List<Usuario> listarTodosUsuarios();
    List<Usuario> listarUsuariosPorRol(Usuario.Rol rol);
}