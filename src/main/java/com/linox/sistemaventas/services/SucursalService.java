package com.linox.sistemaventas.services;

import com.linox.sistemaventas.models.Sucursal;

import java.util.List;
import java.util.Optional;

public interface SucursalService {

    List<Sucursal> findAllByEstadoActivo();
    List<Sucursal> findAllActivos();

    Optional<Sucursal> findById(Integer id);

    Sucursal save(Sucursal sucursal);

    void eliminarPorId(Integer id);
}
