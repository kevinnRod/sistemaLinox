package com.linox.sistemaventas.services.impl;

import com.linox.sistemaventas.models.CategoriaProducto;
import com.linox.sistemaventas.repositories.CategoriaProductoRepository;
import com.linox.sistemaventas.services.CategoriaProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoriaProductoServiceImpl implements CategoriaProductoService {

    @Autowired
    private CategoriaProductoRepository categoriaProductoRepository;

    @Override
    public List<CategoriaProducto> findAll() {
        return categoriaProductoRepository.findAll();
    }

    @Override
    public List<CategoriaProducto> findAllActivos() {
        return categoriaProductoRepository.findByIdEstado(1); // estado 1 = activos
    }

    @Override
    public Optional<CategoriaProducto> findById(Integer id) {
        return categoriaProductoRepository.findById(id);
    }

    @Override
    public CategoriaProducto save(CategoriaProducto categoriaProducto) {
        return categoriaProductoRepository.save(categoriaProducto);
    }

    @Override
    public void deleteById(Integer id) {
        categoriaProductoRepository.deleteById(id); // o eliminación lógica si lo prefieres
    }
}
