// DetalleVentaRepository.java
package com.linox.sistemaventas.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.linox.sistemaventas.models.DetalleVenta;
import com.linox.sistemaventas.models.DetalleVentaId;

@Repository
public interface DetalleVentaRepository extends JpaRepository<DetalleVenta, DetalleVentaId> {
    // You can add custom query methods if needed, for example:
    List<DetalleVenta> findByVentaIdVenta(Integer idVenta);

    List<DetalleVenta> findByProductoId(Integer idProducto);
}