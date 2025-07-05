package com.linox.sistemaventas.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.linox.sistemaventas.models.Rol;

@Repository
public interface RolRepository extends JpaRepository<Rol, Integer> {
    boolean existsByNombreRol(String nombreRol);

    List<Rol> findByIdEstado(Integer idEstado);

    Optional<Rol> findByNombreRol(String nombreRol);
}