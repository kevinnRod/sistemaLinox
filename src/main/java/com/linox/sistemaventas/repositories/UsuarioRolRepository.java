package com.linox.sistemaventas.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.linox.sistemaventas.models.UsuarioRol;
import com.linox.sistemaventas.models.UsuarioRolId;

@Repository
public interface UsuarioRolRepository extends JpaRepository<UsuarioRol, UsuarioRolId> {
    List<UsuarioRol> findByUsuarioIdUsuario(Integer idUsuario);

    List<UsuarioRol> findByRolIdRol(Integer idRol);

    boolean existsByUsuarioIdUsuarioAndRolIdRol(Integer idUsuario, Integer idRol);

    List<UsuarioRol> findByUsuarioIdUsuarioAndIdEstado(Integer idUsuario, Integer idEstado);

    Optional<UsuarioRol> findByUsuarioIdUsuarioAndRolIdRol(Integer idUsuario, Integer idRol);
}