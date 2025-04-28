package com.linox.sistemaventas.services;

import java.util.List;
import java.util.Optional;

import com.linox.sistemaventas.models.Persona;

public interface PersonaService {
    List<Persona> findAll();

    Optional<Persona> findById(Integer id);

    Optional<Persona> findByDni(String dni);

    Persona save(Persona persona);

    void deleteById(Integer id);
}