package com.linox.sistemaventas.repositories;

import com.linox.sistemaventas.entities.UsuarioRol;
import com.linox.sistemaventas.entities.UsuarioRolId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsuarioRolRepository extends JpaRepository<UsuarioRol, UsuarioRolId> {
    List<UsuarioRol> findByUsuarioIdUsuario(Integer idUsuario);

    List<UsuarioRol> findByRolIdRol(Integer idRol);

    boolean existsByUsuarioIdUsuarioAndRolIdRol(Integer idUsuario, Integer idRol);
}