package com.linox.sistemaventas.services;

import com.linox.sistemaventas.models.Kardex;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;

public interface KardexService {
    List<Kardex> findAll();
    List<Kardex> findAllActivos();
    Optional<Kardex> findById(Integer id);
    Kardex save(Kardex kardex);
    void deleteLogico(Integer id);

    Page<Kardex> buscarKardex(String producto, String tipo, LocalDate fechaInicio, LocalDate fechaFin, int page, int size);
}
