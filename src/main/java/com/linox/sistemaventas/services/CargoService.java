package com.linox.sistemaventas.services;

import java.util.List;
import java.util.Optional;

import com.linox.sistemaventas.models.Cargo;

public interface CargoService {
    List<Cargo> listar();

    List<Cargo> findAllActivos();

    Optional<Cargo> findById(Integer id);

    Optional<Cargo> obtenerPorId(Integer id);

    Cargo guardar(Cargo cargo);

    void eliminar(Integer id);

    boolean existePorNombre(String nombreCargo);

    Optional<Cargo> buscarPorNombre(String nombreCargo);
}
