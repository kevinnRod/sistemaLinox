package com.linox.sistemaventas.repositories;

import com.linox.sistemaventas.entities.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RolRepository extends JpaRepository<Rol, Integer> {
    boolean existsByNombreRol(String nombreRol);
}