package com.linox.sistemaventas.services;

import com.linox.sistemaventas.entities.Rol;
import java.util.List;
import java.util.Optional;

public interface RolService {
    List<Rol> findAll();

    Optional<Rol> findById(Integer id);

    Rol save(Rol rol);

    void deleteById(Integer id);
}