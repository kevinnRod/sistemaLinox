// DetalleVentaRepository.java
package com.linox.sistemaventas.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.linox.sistemaventas.models.DetalleVenta;
import com.linox.sistemaventas.models.DetalleVentaId;
import org.springframework.data.domain.Pageable;

@Repository
public interface DetalleVentaRepository extends JpaRepository<DetalleVenta, DetalleVentaId> {
  // You can add custom query methods if needed, for example:
  List<DetalleVenta> findByVentaIdVenta(Integer idVenta);

  List<DetalleVenta> findByProductoId(Integer idProducto);

  @Query("""
          SELECT dv.producto.nombreProducto, SUM(dv.cantidad) as totalVendido
          FROM DetalleVenta dv
          JOIN dv.venta v
          WHERE MONTH(v.fechaV) = MONTH(CURRENT_DATE)
            AND YEAR(v.fechaV) = YEAR(CURRENT_DATE)
            AND v.idEstado = 1
          GROUP BY dv.producto.nombreProducto
          ORDER BY totalVendido DESC
          LIMIT 5
      """)
  List<Object[]> obtenerTop10ProductosMasVendidosDelMes();

  @Query("""
          SELECT dv.producto.nombreProducto, SUM(dv.subtotal) AS totalImporte
          FROM DetalleVenta dv
          JOIN dv.venta v
          WHERE v.idEstado = 1
            AND FUNCTION('MONTH', v.fechaV) = FUNCTION('MONTH', CURRENT_DATE)
            AND FUNCTION('YEAR', v.fechaV) = FUNCTION('YEAR', CURRENT_DATE)
          GROUP BY dv.producto.nombreProducto
          ORDER BY totalImporte DESC
      """)
  List<Object[]> obtenerProductosConMayorImporteUltimoMes(Pageable pageable);
}