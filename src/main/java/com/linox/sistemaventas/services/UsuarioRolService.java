package com.linox.sistemaventas.services;

import java.util.List;
import java.util.Optional;

import com.linox.sistemaventas.models.UsuarioRol;
import com.linox.sistemaventas.models.UsuarioRolId;

public interface UsuarioRolService {
    List<UsuarioRol> findAll();

    Optional<UsuarioRol> findById(UsuarioRolId id);

    UsuarioRol save(UsuarioRol usuarioRol);

    void deleteById(UsuarioRolId id);

    List<UsuarioRol> findByUsuarioId(Integer idUsuario);
}