package com.linox.sistemaventas.services;

import java.util.List;
import java.util.Optional;

import com.linox.sistemaventas.models.CategoriaProducto;

public interface CategoriaProductoService {
    List<CategoriaProducto> findAll();

    List<CategoriaProducto> findAllActivos();

    Optional<CategoriaProducto> findById(Integer id);

    CategoriaProducto save(CategoriaProducto categoriaProducto);

    void deleteById(Integer id); 
}
