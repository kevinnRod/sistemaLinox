package com.linox.sistemaventas.services.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.linox.sistemaventas.models.Producto;
import com.linox.sistemaventas.repositories.ProductoRepository;
import com.linox.sistemaventas.services.ProductoService;

@Service
public class ProductoServiceImpl implements ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    @Override
    public List<Producto> findAll() {
        return productoRepository.findAll();
    }

    @Override
    public List<Producto> findAllActivos() {
        return productoRepository.findByIdEstado(1); // 1 = activos
    }

    @Override
    public Optional<Producto> findById(Integer id) {
        return productoRepository.findById(id);
    }

    @Override
    public Producto save(Producto producto) {
        return productoRepository.save(producto);
    }

    @Override
    public void deleteById(Integer id) {
        productoRepository.deleteById(id); // Cambiar por eliminación lógica si es necesario
    }

    @Override
    public List<Producto> findByCodProductoAndIdEstado(String codProducto, Integer idEstado) {
        return productoRepository.findByCodProductoAndIdEstado(codProducto, idEstado);
    }

    @Override
    public long count() {
        return productoRepository.count();
    }
}
