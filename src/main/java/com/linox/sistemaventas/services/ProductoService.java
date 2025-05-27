package com.linox.sistemaventas.services;

import java.util.List;
import java.util.Optional;

import com.linox.sistemaventas.models.Producto;

public interface ProductoService {
    List<Producto> findAll();

    List<Producto> findAllActivos();

    Optional<Producto> findById(Integer id);

    Producto save(Producto producto);

    void deleteById(Integer id);

    List<Producto> findByCodProductoAndIdEstado(String codProducto, Integer idEstado);

    long count();

}
