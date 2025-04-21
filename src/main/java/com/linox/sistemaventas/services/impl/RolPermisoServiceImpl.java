package com.linox.sistemaventas.services.impl;

import com.linox.sistemaventas.entities.RolPermiso;
import com.linox.sistemaventas.entities.RolPermisoId;
import com.linox.sistemaventas.repositories.RolPermisoRepository;
import com.linox.sistemaventas.services.RolPermisoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RolPermisoServiceImpl implements RolPermisoService {

    @Autowired
    private RolPermisoRepository rolPermisoRepository;

    @Override
    public List<RolPermiso> findAll() {
        return rolPermisoRepository.findAll();
    }

    @Override
    public Optional<RolPermiso> findById(RolPermisoId id) {
        return rolPermisoRepository.findById(id);
    }

    @Override
    public RolPermiso save(RolPermiso rolPermiso) {
        return rolPermisoRepository.save(rolPermiso);
    }

    @Override
    public void deleteById(RolPermisoId id) {
        rolPermisoRepository.deleteById(id);
    }

    @Override
    public List<RolPermiso> findByRolId(Integer idRol) {
        return rolPermisoRepository.findByRolIdRol(idRol);
    }
}