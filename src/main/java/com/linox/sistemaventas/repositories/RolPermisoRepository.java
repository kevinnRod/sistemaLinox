package com.linox.sistemaventas.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.linox.sistemaventas.models.RolPermiso;
import com.linox.sistemaventas.models.RolPermisoId;

@Repository
public interface RolPermisoRepository extends JpaRepository<RolPermiso, RolPermisoId> {
    List<RolPermiso> findByRolIdRol(Integer idRol);

    List<RolPermiso> findByPermisoIdPermiso(Integer idPermiso);

    boolean existsByRolIdRolAndPermisoIdPermiso(Integer idRol, Integer idPermiso);

    Optional<RolPermiso> findByRolIdRolAndPermisoIdPermiso(Integer idRol, Integer idPermiso);

    List<RolPermiso> findByRolIdRolAndIdEstado(Integer idRol, Integer idEstado);
}