package com.linox.sistemaventas.services;

import com.linox.sistemaventas.entities.Usuario;

import java.util.List;
import java.util.Optional;

public interface UsuarioService {
    List<Usuario> findAll();

    Optional<Usuario> findById(Integer id);

    Usuario save(Usuario usuario);

    void deleteById(Integer id);

    Optional<Usuario> findByUsuario(String usuario);

    Optional<Usuario> findByCorreo(String correo);
}