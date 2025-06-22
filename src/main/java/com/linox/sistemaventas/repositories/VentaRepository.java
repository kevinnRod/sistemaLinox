package com.linox.sistemaventas.repositories;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.linox.sistemaventas.models.Venta;

public interface VentaRepository extends JpaRepository<Venta, Integer> {
        List<Venta> findByIdEstado(Integer idEstado);

        Optional<Venta> findByCodVenta(String codVenta);

        @Query("""
                            SELECT FUNCTION('MONTH', v.fechaV), SUM(v.total)
                            FROM Venta v
                            WHERE v.idEstado = 1
                            GROUP BY FUNCTION('MONTH', v.fechaV)
                        """)
        List<Object[]> obtenerVentasPorMes();

        @Query("SELECT MONTH(v.fechaV) AS mes, SUM(v.total) AS totalMensual " +
                        "FROM Venta v WHERE v.empleado.idPersona = :idEmpleado AND v.idEstado = 1 " +
                        "GROUP BY MONTH(v.fechaV) ORDER BY mes")
        List<Object[]> obtenerTotalesPorMes(@Param("idEmpleado") Integer idEmpleado);

        List<Venta> findByFechaVBetweenAndIdEstado(LocalDateTime inicio, LocalDateTime fin, Integer estado);

        long countByFechaVBetweenAndIdEstado(LocalDateTime inicio, LocalDateTime fin, Integer estado);

        @Query("SELECT SUM(v.total) FROM Venta v WHERE v.fechaV BETWEEN :inicio AND :fin AND v.idEstado = :estado")
        Optional<BigDecimal> sumTotalByFechaVBetweenAndIdEstado(@Param("inicio") LocalDateTime inicio,
                        @Param("fin") LocalDateTime fin,
                        @Param("estado") Integer estado);

        long countByFechaVBetween(LocalDateTime inicio, LocalDateTime fin);

        // Repositorio
        @Query("SELECT SUM(v.total) FROM Venta v WHERE v.fechaV BETWEEN :inicio AND :fin")
        Optional<BigDecimal> totalVentasEntreFechas(@Param("inicio") LocalDateTime inicio,
                        @Param("fin") LocalDateTime fin);

        @Query("SELECT dv.producto.nombreProducto FROM DetalleVenta dv GROUP BY dv.producto.nombreProducto ORDER BY SUM(dv.cantidad) DESC")
        List<String> obtenerProductoMasVendidoNombre(Pageable pageable);

        @Query("SELECT SUM(dv.cantidad) FROM Venta v JOIN v.detallesVenta dv GROUP BY dv.producto.nombreProducto ORDER BY SUM(dv.cantidad) DESC")
        List<Integer> obtenerProductoMasVendidoCantidad(Pageable pageable);

}
