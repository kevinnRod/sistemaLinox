package com.linox.sistemaventas.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.linox.sistemaventas.models.Permiso;

@Repository
public interface PermisoRepository extends JpaRepository<Permiso, Integer> {
    boolean existsByNombrePermiso(String nombrePermiso);

    List<Permiso> findByIdEstado(Integer idEstado);
}