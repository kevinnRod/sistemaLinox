package com.linox.sistemaventas.repositories;

import com.linox.sistemaventas.entities.Permiso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermisoRepository extends JpaRepository<Permiso, Integer> {
    boolean existsByNombrePermiso(String nombrePermiso);
}