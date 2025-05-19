package com.linox.sistemaventas.services.impl;

import com.linox.sistemaventas.models.CategoriaCliente;
import com.linox.sistemaventas.repositories.CategoriaClienteRepository;
import com.linox.sistemaventas.services.CategoriaClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoriaClienteServiceImpl implements CategoriaClienteService {

    @Autowired
    private CategoriaClienteRepository categoriaClienteRepository;

    @Override
    public List<CategoriaCliente> findAll() {
        return categoriaClienteRepository.findAll();
    }

    @Override
    public List<CategoriaCliente> findAllActivos() {
        return categoriaClienteRepository.findByIdEstado(1); // estado 1 = activos
    }

    @Override
    public Optional<CategoriaCliente> findById(Integer id) {
        return categoriaClienteRepository.findById(id);
    }

    @Override
    public CategoriaCliente save(CategoriaCliente categoriaCliente) {
        return categoriaClienteRepository.save(categoriaCliente);
    }

    @Override
    public void deleteById(Integer id) {
        categoriaClienteRepository.deleteById(id); // o eliminación lógica
    }
}
