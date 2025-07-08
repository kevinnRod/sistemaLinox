package com.linox.sistemaventas.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.linox.sistemaventas.models.Producto;

public interface ProductoRepository extends JpaRepository<Producto, Integer> {
    List<Producto> findByIdEstado(Integer estado);

    List<Producto> findByCodProductoAndIdEstado(String codProducto, Integer idEstado);

    @Query("SELECT p FROM Producto p WHERE p.stock <= 60 AND p.idEstado = 1")
    List<Producto> findProductosConBajoStock();

    int countByIdEstado(Integer estado);

    int countByStockLessThanAndIdEstado(Integer stock, Integer estado);

    @Query("SELECT p FROM Producto p WHERE " +
        "(:nombre IS NULL OR LOWER(p.nombreProducto) LIKE LOWER(CONCAT('%', :nombre, '%'))) AND " +
        "(:categoriaId IS NULL OR p.categoria.id = :categoriaId) AND " +
        "(:sucursalId IS NULL OR p.sucursal.idSucursal = :sucursalId)")
    Page<Producto> findByFilters(@Param("nombre") String nombre,
                                @Param("categoriaId") Integer categoriaId,
                                @Param("sucursalId") Integer sucursalId,
                                Pageable pageable);

}
