package com.transporte.service.impl;

import com.transporte.model.Usuario;
import com.transporte.repository.UsuarioRepository;
import com.transporte.service.UsuarioService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioServiceImpl(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Usuario registrarUsuario(Usuario usuario) {
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        return usuarioRepository.save(usuario);
    }

    @Override
    public Usuario actualizarUsuario(Usuario usuario) {
        return null;
    }

    @Override
    public void eliminarUsuario(Long id) {

    }

    @Override
    public Usuario obtenerUsuarioPorId(Long id) {
        return null;
    }

    @Override
    public List<Usuario> listarTodosUsuarios() {
        return List.of();
    }

    @Override
    public List<Usuario> listarUsuariosPorRol(Usuario.Rol rol) {
        return List.of();
    }

    // Implementación de otros métodos...
}