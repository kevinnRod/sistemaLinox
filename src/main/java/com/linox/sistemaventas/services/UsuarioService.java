package com.linox.sistemaventas.services;

import java.util.List;
import java.util.Optional;

import com.linox.sistemaventas.models.Usuario;

public interface UsuarioService {
    List<Usuario> findAll();

    Optional<Usuario> findById(Integer idUsuario);

    Usuario save(Usuario usuario);

    void deleteById(Integer idUsuario);

    Optional<Usuario> findByUsuario(String usuario);

    Optional<Usuario> findByCorreo(String correo);

    List<Usuario> findAllByEstadoActivo();
}