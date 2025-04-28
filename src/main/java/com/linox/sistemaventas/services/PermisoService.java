package com.linox.sistemaventas.services;

import java.util.List;
import java.util.Optional;

import com.linox.sistemaventas.models.Permiso;

public interface PermisoService {
    List<Permiso> findAll();

    Optional<Permiso> findById(Integer id);

    Permiso save(Permiso permiso);

    void deleteById(Integer id);

    List<Permiso> findAllByEstadoActivo();
}