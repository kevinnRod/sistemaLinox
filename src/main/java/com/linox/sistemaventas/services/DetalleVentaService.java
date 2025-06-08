// DetalleVentaService.java
package com.linox.sistemaventas.services;

import java.util.List;
import java.util.Optional;

import com.linox.sistemaventas.models.DetalleVenta;
import com.linox.sistemaventas.models.DetalleVentaId;

public interface DetalleVentaService {
    List<DetalleVenta> findAllDetallesVenta();

    Optional<DetalleVenta> findDetalleVentaById(DetalleVentaId id);

    DetalleVenta saveDetalleVenta(DetalleVenta detalleVenta);

    void deleteDetalleVenta(DetalleVentaId id);

    List<DetalleVenta> findDetallesByVentaId(Integer idVenta);
}