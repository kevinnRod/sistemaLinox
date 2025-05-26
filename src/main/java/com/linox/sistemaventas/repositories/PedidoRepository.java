package com.linox.sistemaventas.repositories;

import com.linox.sistemaventas.models.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PedidoRepository extends JpaRepository<Pedido, Integer> {
    List<Pedido> findByEstado(Integer estado); // 1 = activo
}
