package com.linox.sistemaventas.services;

import com.linox.sistemaventas.entities.RolPermiso;
import com.linox.sistemaventas.entities.RolPermisoId;

import java.util.List;
import java.util.Optional;

public interface RolPermisoService {
    List<RolPermiso> findAll();

    Optional<RolPermiso> findById(RolPermisoId id);

    RolPermiso save(RolPermiso rolPermiso);

    void deleteById(RolPermisoId id);

    List<RolPermiso> findByRolId(Integer idRol);
}