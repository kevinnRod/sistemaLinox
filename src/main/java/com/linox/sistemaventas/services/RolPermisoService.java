package com.linox.sistemaventas.services;

import java.util.List;
import java.util.Optional;

import com.linox.sistemaventas.models.Permiso;
import com.linox.sistemaventas.models.Rol;
import com.linox.sistemaventas.models.RolPermiso;
import com.linox.sistemaventas.models.RolPermisoId;

public interface RolPermisoService {
    List<RolPermiso> findAll();

    Optional<RolPermiso> findById(RolPermisoId id);

    RolPermiso save(RolPermiso rolPermiso);

    void deleteById(RolPermisoId id);

    List<RolPermiso> findByRolId(Integer idRol);

    List<RolPermiso> findAllByEstadoActivo(Integer idRol);

    boolean existsByRolPermiso(Rol rol, Permiso permiso);

    Optional<RolPermiso> findByRolAndPermiso(Rol rol, Permiso permiso);
}