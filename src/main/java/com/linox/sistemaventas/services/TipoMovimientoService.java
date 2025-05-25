package com.linox.sistemaventas.services;

import com.linox.sistemaventas.models.TipoMovimiento;

import java.util.List;
import java.util.Optional;

public interface TipoMovimientoService {

    List<TipoMovimiento> findAll();

    List<TipoMovimiento> findAllActivos();

    Optional<TipoMovimiento> findById(Integer id);

    TipoMovimiento save(TipoMovimiento tipoMovimiento);

    void deleteById(Integer id);
}
