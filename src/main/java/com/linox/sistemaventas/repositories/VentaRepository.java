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
    @Query("SELECT SUM(v.total) FROM Venta v WHERE v.fechaV BETWEEN :inicio AND :fin AND v.idEstado = 1")
    Optional<BigDecimal> totalVentasEntreFechas(@Param("inicio") LocalDateTime inicio,
            @Param("fin") LocalDateTime fin);

    @Query("SELECT dv.producto.nombreProducto FROM DetalleVenta dv GROUP BY dv.producto.nombreProducto ORDER BY SUM(dv.cantidad) DESC")
    List<String> obtenerProductoMasVendidoNombre(Pageable pageable);

    @Query("SELECT SUM(dv.cantidad) FROM Venta v JOIN v.detallesVenta dv GROUP BY dv.producto.nombreProducto ORDER BY SUM(dv.cantidad) DESC")
    List<Integer> obtenerProductoMasVendidoCantidad(Pageable pageable);

    @Query(value = """
                SELECT
                    COALESCE(CONCAT(p.nombres, ' ', p.apellidos), e.razon_social) AS cliente,
                    SUM(v.total) AS monto
                FROM venta v
                LEFT JOIN cliente c ON v.cod_cliente = c.cod_cliente
                LEFT JOIN cliente_natural cn ON c.cod_cliente = cn.cod_cliente
                LEFT JOIN persona p ON cn.id_persona = p.id_persona
                LEFT JOIN cliente_juridico cj ON c.cod_cliente = cj.cod_cliente
                LEFT JOIN empresa e ON cj.id_empresa = e.id_empresa
                GROUP BY cliente
                ORDER BY monto DESC
            """, nativeQuery = true)
    List<Object[]> obtenerVentasPorCliente();

    @Query(value = """
                SELECT
                    COALESCE(CONCAT(p.nombres, ' ', p.apellidos), e.razon_social) AS cliente,
                    SUM(v.total) AS monto
                FROM venta v
                LEFT JOIN cliente c ON v.cod_cliente = c.cod_cliente
                LEFT JOIN cliente_natural cn ON c.cod_cliente = cn.cod_cliente
                LEFT JOIN persona p ON cn.id_persona = p.id_persona
                LEFT JOIN cliente_juridico cj ON c.cod_cliente = cj.cod_cliente
                LEFT JOIN empresa e ON cj.id_empresa = e.id_empresa
                WHERE v.fechaV BETWEEN :inicio AND :fin
                GROUP BY cliente
                ORDER BY monto DESC
            """, nativeQuery = true)
    List<Object[]> obtenerVentasPorClienteEnRango(@Param("inicio") LocalDateTime inicio,
            @Param("fin") LocalDateTime fin);

        @Query(value="""
                SELECT v.cod_cliente AS CLIENTE,SUM(v.total) AS TOTAL
                FROM venta v
                WHERE v.fechav >= DATE_SUB(CURDATE(), INTERVAL 1 MONTH)
                GROUP BY(v.cod_cliente);
                
                """,nativeQuery = true)
        List<Object[]> obtenerVentasUltimoMes();

        @Query(value="""
                        SELECT p.nombre_producto AS PRODUCTO,SUM(d.subtotal) AS TOTAL
                        FROM venta v
                        JOIN detalle_venta d ON d.id_venta = v.id_venta
                        JOIN producto p ON p.id_producto = d.id_producto
                        WHERE v.fechav >= DATE_SUB(CURDATE(), INTERVAL 1 MONTH)
                        GROUP BY(p.nombre_producto);
                        """,nativeQuery = true)
        List<Object[]> obtenerMontoProductosVend();


        @Query(value = """
                        SELECT nombre_producto as PRODUCTO,stock as STOCK  FROM producto WHERE stock<15;
                        """,nativeQuery = true)
        List<Object[]> obtenerProdStockBajo();
}
