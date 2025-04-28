package com.linox.sistemaventas.services.impl;

import com.linox.sistemaventas.models.Rol;
import com.linox.sistemaventas.repositories.RolRepository;
import com.linox.sistemaventas.services.RolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RolServiceImpl implements RolService {

    @Autowired
    private RolRepository rolRepository;

    @Override
    public List<Rol> findAll() {
        return rolRepository.findAll();
    }

    @Override
    public Optional<Rol> findById(Integer id) {
        return rolRepository.findById(id);
    }

    @Override
    public Rol save(Rol rol) {
        return rolRepository.save(rol);
    }

    @Override
    public void deleteById(Integer id) {
        rolRepository.deleteById(id);
    }

    @Override
    public List<Rol> findAllByEstadoActivo() {
        return rolRepository.findByIdEstado(1); // 1 es el estado activo
    }
}