package com.linox.sistemaventas.services;

import com.linox.sistemaventas.entities.UsuarioRol;
import com.linox.sistemaventas.entities.UsuarioRolId;

import java.util.List;
import java.util.Optional;

public interface UsuarioRolService {
    List<UsuarioRol> findAll();

    Optional<UsuarioRol> findById(UsuarioRolId id);

    UsuarioRol save(UsuarioRol usuarioRol);

    void deleteById(UsuarioRolId id);

    List<UsuarioRol> findByUsuarioId(Integer idUsuario);
}