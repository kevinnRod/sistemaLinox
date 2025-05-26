package com.linox.sistemaventas.services.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.linox.sistemaventas.models.Cliente;
import com.linox.sistemaventas.models.Sucursal;
import com.linox.sistemaventas.repositories.ClienteRepository;
import com.linox.sistemaventas.services.ClienteService;

@Service
public class ClienteServiceImpl implements ClienteService {

    @Autowired
    private final ClienteRepository clienteRepository;

    public ClienteServiceImpl(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @Override
    public List<Cliente> findAll() {
        return clienteRepository.findAll();
    }

    @Override
    public Optional<Cliente> findById(Integer id) {
        return clienteRepository.findById(id);
    }

    @Override
    public Cliente save(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    @Override
    public void deleteById(Integer id) {
        clienteRepository.deleteById(id);
    }

    @Override
    public List<Cliente> findAllByEstadoActivo() {
        return clienteRepository.findByIdEstado(1); // 1 es el estado activo
    }

    @Override
    public long countAll() {
        return clienteRepository.count();
    }

    @Override
    public List<Cliente> findAllActivos() {
        return clienteRepository.findByIdEstado(1); 
    }
}
