package com.linox.sistemaventas.services.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.linox.sistemaventas.models.Permiso;
import com.linox.sistemaventas.models.Rol;
import com.linox.sistemaventas.models.RolPermiso;
import com.linox.sistemaventas.models.RolPermisoId;
import com.linox.sistemaventas.repositories.RolPermisoRepository;
import com.linox.sistemaventas.services.RolPermisoService;

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

    @Override
    public List<RolPermiso> findAllByEstadoActivo(Integer idRol) {
        return rolPermisoRepository.findByRolIdRolAndIdEstado(idRol, 1);
    }

    @Override
    public boolean existsByRolPermiso(Rol rol, Permiso permiso) {
        return rolPermisoRepository.existsByRolIdRolAndPermisoIdPermiso(rol.getIdRol(), permiso.getIdPermiso());
    }

    @Override
    public Optional<RolPermiso> findByRolAndPermiso(Rol rol, Permiso permiso) {
        return rolPermisoRepository.findByRolIdRolAndPermisoIdPermiso(rol.getIdRol(), permiso.getIdPermiso());
    }
}
