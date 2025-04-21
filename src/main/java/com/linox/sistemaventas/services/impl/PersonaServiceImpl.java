package com.linox.sistemaventas.services.impl;

import com.linox.sistemaventas.entities.Persona;
import com.linox.sistemaventas.repositories.PersonaRepository;
import com.linox.sistemaventas.services.PersonaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PersonaServiceImpl implements PersonaService {

    @Autowired
    private PersonaRepository personaRepository;

    @Override
    public List<Persona> findAll() {
        return personaRepository.findAll();
    }

    @Override
    public Optional<Persona> findById(Integer id) {
        return personaRepository.findById(id);
    }

    @Override
    public Optional<Persona> findByDni(String dni) {
        return personaRepository.findByDni(dni);
    }

    @Override
    public Persona save(Persona persona) {
        return personaRepository.save(persona);
    }

    @Override
    public void deleteById(Integer id) {
        personaRepository.deleteById(id);
    }
}