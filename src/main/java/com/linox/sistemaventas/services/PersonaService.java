package com.linox.sistemaventas.services;

import com.linox.sistemaventas.entities.Persona;

import java.util.List;
import java.util.Optional;

public interface PersonaService {
    List<Persona> findAll();

    Optional<Persona> findById(Integer id);

    Optional<Persona> findByDni(String dni);

    Persona save(Persona persona);

    void deleteById(Integer id);
}