package com.linox.sistemaventas.services;

import com.linox.sistemaventas.models.Proveedor;

import java.util.List;
import java.util.Optional;

public interface ProveedorService {
    List<Proveedor> findAll();
    List<Proveedor> findAllActivos();
    Optional<Proveedor> findById(Integer id);
    Proveedor save(Proveedor proveedor);
    void deleteById(Integer id);
}
