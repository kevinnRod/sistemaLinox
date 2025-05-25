package com.linox.sistemaventas.services.impl;

import com.linox.sistemaventas.models.Proveedor;
import com.linox.sistemaventas.repositories.ProveedorRepository;
import com.linox.sistemaventas.services.ProveedorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProveedorServiceImpl implements ProveedorService {

    @Autowired
    private ProveedorRepository proveedorRepository;

    @Override
    public List<Proveedor> findAll() {
        return proveedorRepository.findAll();
    }

    @Override
    public List<Proveedor> findAllActivos() {
        return proveedorRepository.findByIdEstado(1); // 1 = Activo
    }

    @Override
    public Optional<Proveedor> findById(Integer id) {
        return proveedorRepository.findById(id);
    }

    @Override
    public Proveedor save(Proveedor proveedor) {
        return proveedorRepository.save(proveedor);
    }

    @Override
    public void deleteById(Integer id) {
        proveedorRepository.deleteById(id); // o eliminación lógica si se prefiere
    }
}
