package com.linox.sistemaventas.services.impl;

import com.linox.sistemaventas.models.TipoMovimiento;
import com.linox.sistemaventas.repositories.TipoMovimientoRepository;
import com.linox.sistemaventas.services.TipoMovimientoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TipoMovimientoServiceImpl implements TipoMovimientoService {

    @Autowired
    private TipoMovimientoRepository tipoMovimientoRepository;

    @Override
    public List<TipoMovimiento> findAll() {
        return tipoMovimientoRepository.findAll();
    }

    @Override
    public List<TipoMovimiento> findAllActivos() {
        return tipoMovimientoRepository.findByIdEstado(1); // Estado 1 = Activo
    }

    @Override
    public Optional<TipoMovimiento> findById(Integer id) {
        return tipoMovimientoRepository.findById(id);
    }

    @Override
    public Optional<TipoMovimiento> findByCodigo(String codigo){
        return tipoMovimientoRepository.findByCodigo(codigo);
    }
    

    @Override
    public TipoMovimiento save(TipoMovimiento tipoMovimiento) {
        return tipoMovimientoRepository.save(tipoMovimiento);
    }

    @Override
    public void deleteById(Integer id) {
        tipoMovimientoRepository.deleteById(id); // Si implementas eliminación lógica, cambia esto
    }
}
