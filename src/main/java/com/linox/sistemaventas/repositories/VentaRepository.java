package com.linox.sistemaventas.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

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

}
