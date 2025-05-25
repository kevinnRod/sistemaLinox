package com.linox.sistemaventas.services.impl;

import com.linox.sistemaventas.models.UnidadMedida;
import com.linox.sistemaventas.repositories.UnidadMedidaRepository;
import com.linox.sistemaventas.services.UnidadMedidaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UnidadMedidaServiceImpl implements UnidadMedidaService {

    @Autowired
    private UnidadMedidaRepository unidadMedidaRepository;

    @Override
    public List<UnidadMedida> findAll() {
        return unidadMedidaRepository.findAll();
    }

    @Override
    public List<UnidadMedida> findAllActivos() {
        return unidadMedidaRepository.findByIdEstado(1); // 1 = activos
    }

    @Override
    public Optional<UnidadMedida> findById(Integer id) {
        return unidadMedidaRepository.findById(id);
    }

    @Override
    public UnidadMedida save(UnidadMedida unidadMedida) {
        return unidadMedidaRepository.save(unidadMedida);
    }

    @Override
    public void deleteById(Integer id) {
        unidadMedidaRepository.deleteById(id); // O eliminación lógica si prefieres
    }
}
