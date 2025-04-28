package com.linox.sistemaventas.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.linox.sistemaventas.models.Persona;

import java.util.Optional;

@Repository
public interface PersonaRepository extends JpaRepository<Persona, Integer> {
    Optional<Persona> findByDni(String dni);

    Optional<Persona> findByCorreo(String correo);

    boolean existsByDni(String dni);

    boolean existsByCorreo(String correo);
}