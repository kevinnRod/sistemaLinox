package com.linox.sistemaventas.services;

import com.linox.sistemaventas.models.Cargo;

import java.util.List;
import java.util.Optional;

public interface CargoService {
    List<Cargo> listar();

    Optional<Cargo> obtenerPorId(Integer id);

    Cargo guardar(Cargo cargo);

    void eliminar(Integer id);

    boolean existePorNombre(String nombreCargo);
}
