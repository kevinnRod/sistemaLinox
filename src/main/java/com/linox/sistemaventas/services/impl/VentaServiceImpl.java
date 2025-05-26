package com.linox.sistemaventas.services.impl;

import com.linox.sistemaventas.models.Venta;
import com.linox.sistemaventas.repositories.VentaRepository;
import com.linox.sistemaventas.services.VentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VentaServiceImpl implements VentaService {

    @Autowired
    private VentaRepository ventaRepository;

    @Override
    public List<Venta> findAll() {
        return ventaRepository.findAll();
    }

    @Override
    public List<Venta> findAllActivas() {
        return ventaRepository.findByIdEstado(1);
    }

    @Override
    public Optional<Venta> findById(Integer id) {
        return ventaRepository.findById(id);
    }

    @Override
    public Venta save(Venta venta) {
        return ventaRepository.save(venta);
    }

    @Override
    public void deleteLogico(Integer id) {
        ventaRepository.findById(id).ifPresent(v -> {
            v.setIdEstado(2); // Inactivo
            ventaRepository.save(v);
        });
    }
}
