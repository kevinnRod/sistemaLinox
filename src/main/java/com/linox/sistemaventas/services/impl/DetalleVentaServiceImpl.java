// DetalleVentaServiceImpl.java
package com.linox.sistemaventas.services.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.linox.sistemaventas.models.DetalleVenta;
import com.linox.sistemaventas.models.DetalleVentaId;
import com.linox.sistemaventas.repositories.DetalleVentaRepository;
import com.linox.sistemaventas.services.DetalleVentaService;

@Service
public class DetalleVentaServiceImpl implements DetalleVentaService {

    private final DetalleVentaRepository detalleVentaRepository;

    public DetalleVentaServiceImpl(DetalleVentaRepository detalleVentaRepository) {
        this.detalleVentaRepository = detalleVentaRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<DetalleVenta> findAllDetallesVenta() {
        return detalleVentaRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DetalleVenta> findDetalleVentaById(DetalleVentaId id) {
        return detalleVentaRepository.findById(id);
    }

    @Override
    @Transactional
    public DetalleVenta saveDetalleVenta(DetalleVenta detalleVenta) {
        // Here you can add business logic before saving:
        // - Validate quantity
        // - Calculate subtotal if not already calculated (quantity * product unit
        // price)
        // - Check product stock
        return detalleVentaRepository.save(detalleVenta);
    }

    @Override
    @Transactional
    public void deleteDetalleVenta(DetalleVentaId id) {
        detalleVentaRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DetalleVenta> findDetallesByVentaId(Integer idVenta) {
        return detalleVentaRepository.findByVentaIdVenta(idVenta);
    }
}