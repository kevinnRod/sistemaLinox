package com.linox.sistemaventas.services;

import com.linox.sistemaventas.entities.Permiso;
import java.util.List;
import java.util.Optional;

public interface PermisoService {
    List<Permiso> findAll();

    Optional<Permiso> findById(Integer id);

    Permiso save(Permiso permiso);

    void deleteById(Integer id);
}