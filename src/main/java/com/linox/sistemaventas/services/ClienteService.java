package com.linox.sistemaventas.services;

import java.util.List;
import java.util.Optional;

import com.linox.sistemaventas.models.Cliente;
import com.linox.sistemaventas.models.Empresa;
import com.linox.sistemaventas.models.Persona;

public interface ClienteService {
    List<Cliente> findAll();

    Optional<Cliente> findById(String cod);

    Cliente save(Cliente cliente);

    void deleteById(String cod);

    List<Cliente> findAllByEstadoActivo();

    List<Cliente> findAllActivos();

    long countAll();

    boolean existsByPersona(Persona persona);

    boolean existsByEmpresa(Empresa empresa);

}
