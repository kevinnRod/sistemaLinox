package com.linox.sistemaventas.services;

import com.linox.sistemaventas.models.UnidadMedida;

import java.util.List;
import java.util.Optional;

public interface UnidadMedidaService {
    List<UnidadMedida> findAll();
    List<UnidadMedida> findAllActivos();
    Optional<UnidadMedida> findById(Integer id);
    UnidadMedida save(UnidadMedida unidadMedida);
    void deleteById(Integer id);
}
