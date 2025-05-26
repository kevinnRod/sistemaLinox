package com.linox.sistemaventas.services;

import java.util.List;
import java.util.Optional;

import com.linox.sistemaventas.models.Persona;
import com.linox.sistemaventas.models.Sucursal;

public interface PersonaService {
    List<Persona> findAll();

    Optional<Persona> findById(Integer id);

    Optional<Persona> findByDni(String dni);

    Persona save(Persona persona);

    void deleteById(Integer id);

    List<Persona> findAllByEstadoActivo();

    List<Persona> findAllActivos();

}