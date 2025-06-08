package com.linox.sistemaventas.services.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.linox.sistemaventas.models.Kardex;
import com.linox.sistemaventas.repositories.KardexRepository;
import com.linox.sistemaventas.services.KardexService;

@Service
public class KardexServiceImpl implements KardexService {

    @Autowired
    private KardexRepository kardexRepository;

    @Override
    public List<Kardex> findAll() {
        return kardexRepository.findAll();
    }

    @Override
    public List<Kardex> findAllActivos() {
        return kardexRepository.findByIdEstado(1); // Solo activos
    }

    @Override
    public Optional<Kardex> findById(Integer id) {
        return kardexRepository.findById(id);
    }

    @Override
    public Kardex save(Kardex kardex) {
        return kardexRepository.save(kardex);
    }

    @Override
    public void deleteLogico(Integer id) {
        kardexRepository.findById(id).ifPresent(k -> {
            k.setIdEstado(2); // Inactivo
            kardexRepository.save(k);
        });
    }
}
