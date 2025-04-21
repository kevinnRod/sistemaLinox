package com.linox.sistemaventas.repositories;

import com.linox.sistemaventas.entities.Persona;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PersonaRepository extends JpaRepository<Persona, Integer> {
    Optional<Persona> findByDni(String dni);

    Optional<Persona> findByCorreo(String correo);

    boolean existsByDni(String dni);

    boolean existsByCorreo(String correo);
}