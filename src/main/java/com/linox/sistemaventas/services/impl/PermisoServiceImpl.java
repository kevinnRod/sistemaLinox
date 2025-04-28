package com.linox.sistemaventas.services.impl;

import com.linox.sistemaventas.models.Permiso;
import com.linox.sistemaventas.repositories.PermisoRepository;
import com.linox.sistemaventas.services.PermisoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PermisoServiceImpl implements PermisoService {

    @Autowired
    private PermisoRepository permisoRepository;

    @Override
    public List<Permiso> findAll() {
        return permisoRepository.findAll();
    }

    @Override
    public Optional<Permiso> findById(Integer id) {
        return permisoRepository.findById(id);
    }

    @Override
    public Permiso save(Permiso permiso) {
        return permisoRepository.save(permiso);
    }

    @Override
    public void deleteById(Integer id) {
        permisoRepository.deleteById(id);
    }

    @Override
    public List<Permiso> findAllByEstadoActivo() {
        return permisoRepository.findByIdEstado(1); // 1 es el estado activo
    }
}