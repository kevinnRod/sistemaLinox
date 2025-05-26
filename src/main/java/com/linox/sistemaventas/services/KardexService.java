package com.linox.sistemaventas.services;

import com.linox.sistemaventas.models.Kardex;

import java.util.List;
import java.util.Optional;

public interface KardexService {
    List<Kardex> findAll();
    List<Kardex> findAllActivos();
    Optional<Kardex> findById(Integer id);
    Kardex save(Kardex kardex);
    void deleteLogico(Integer id);
}
