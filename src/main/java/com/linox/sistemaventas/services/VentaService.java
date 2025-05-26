package com.linox.sistemaventas.services;

import com.linox.sistemaventas.models.Venta;

import java.util.List;
import java.util.Optional;

public interface VentaService {
    List<Venta> findAll();
    List<Venta> findAllActivas();
    Optional<Venta> findById(Integer id);
    Venta save(Venta venta);
    void deleteLogico(Integer id);
}
