package com.linox.sistemaventas.services.impl;

import com.linox.sistemaventas.models.Sucursal;
import com.linox.sistemaventas.repositories.SucursalRepository;
import com.linox.sistemaventas.services.SucursalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SucursalServiceImpl implements SucursalService {

    @Autowired
    private SucursalRepository sucursalRepository;

    @Override
    public List<Sucursal> findAllByEstadoActivo() {
        return sucursalRepository.findByIdEstado(1); // Solo activas
    }

    @Override
    public Optional<Sucursal> findById(Integer id) {
        return sucursalRepository.findById(id);
    }

    @Override
    public Sucursal save(Sucursal sucursal) {
        return sucursalRepository.save(sucursal);
    }

    @Override
    public void eliminarPorId(Integer id) {
        sucursalRepository.deleteById(id);
    }
}
