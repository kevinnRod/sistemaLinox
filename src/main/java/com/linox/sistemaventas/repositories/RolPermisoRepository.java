package com.linox.sistemaventas.repositories;

import com.linox.sistemaventas.entities.RolPermiso;
import com.linox.sistemaventas.entities.RolPermisoId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RolPermisoRepository extends JpaRepository<RolPermiso, RolPermisoId> {
    List<RolPermiso> findByRolIdRol(Integer idRol);

    List<RolPermiso> findByPermisoIdPermiso(Integer idPermiso);

    boolean existsByRolIdRolAndPermisoIdPermiso(Integer idRol, Integer idPermiso);
}