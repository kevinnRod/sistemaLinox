package com.linox.sistemaventas.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.linox.sistemaventas.models.Rol;

@Repository
public interface RolRepository extends JpaRepository<Rol, Integer> {
    boolean existsByNombreRol(String nombreRol);

    List<Rol> findByIdEstado(Integer idEstado);
}