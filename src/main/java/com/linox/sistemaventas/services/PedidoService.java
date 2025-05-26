package com.linox.sistemaventas.services;

import com.linox.sistemaventas.models.Pedido;

import java.util.List;
import java.util.Optional;

public interface PedidoService {
    List<Pedido> findAll();
    List<Pedido> findAllActivos();
    Optional<Pedido> findById(Integer id);
    Pedido save(Pedido pedido);
    void deleteLogico(Integer id);
}
