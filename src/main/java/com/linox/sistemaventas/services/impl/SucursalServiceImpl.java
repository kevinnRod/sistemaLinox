package com.linox.sistemaventas.services.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.linox.sistemaventas.models.Sucursal;
import com.linox.sistemaventas.repositories.SucursalRepository;
import com.linox.sistemaventas.services.SucursalService;

//import groovyjarjarantlr4.v4.parse.ANTLRParser.ruleEntry_return;

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

    @Override
    public List<Sucursal> findAllActivos() {
        return sucursalRepository.findByIdEstado(1);
    }

    @Override
    public boolean existsByNombreSucursal(String nombreSucursal) {
        return sucursalRepository.existsByNombreSucursal(nombreSucursal);
    }

    @Override
    public Optional<Sucursal> findByNombreSucursal(String nombreSucursal) {
        return sucursalRepository.findByNombreSucursal(nombreSucursal);
    }
}
