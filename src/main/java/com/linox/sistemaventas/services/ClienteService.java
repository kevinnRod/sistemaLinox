package com.linox.sistemaventas.services;

import com.linox.sistemaventas.models.Cliente;

import java.util.List;
import java.util.Optional;

public interface ClienteService {
    List<Cliente> findAll();

    Optional<Cliente> findById(Integer id);

    Cliente save(Cliente cliente);

    void deleteById(Integer id);

    List<Cliente> findAllByEstadoActivo();
    List<Cliente> findAllActivos();

    long countAll();
}
