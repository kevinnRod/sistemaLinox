package com.linox.sistemaventas.services.impl;

import com.linox.sistemaventas.models.Pedido;
import com.linox.sistemaventas.repositories.PedidoRepository;
import com.linox.sistemaventas.services.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PedidoServiceImpl implements PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Override
    public List<Pedido> findAll() {
        return pedidoRepository.findAll();
    }

    @Override
    public List<Pedido> findAllActivos() {
        return pedidoRepository.findByEstado(1); // 1 = activo
    }

    @Override
    public Optional<Pedido> findById(Integer id) {
        return pedidoRepository.findById(id);
    }

    @Override
    public Pedido save(Pedido pedido) {
        return pedidoRepository.save(pedido);
    }

    @Override
    public void deleteLogico(Integer id) {
        pedidoRepository.findById(id).ifPresent(p -> {
            p.setEstado(2); // inactivo
            pedidoRepository.save(p);
        });
    }
}
