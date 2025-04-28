package com.linox.sistemaventas.services;

import java.util.List;
import java.util.Optional;

import com.linox.sistemaventas.models.Rol;

public interface RolService {
    List<Rol> findAll();

    Optional<Rol> findById(Integer id);

    Rol save(Rol rol);

    void deleteById(Integer id);

    List<Rol> findAllByEstadoActivo();
}