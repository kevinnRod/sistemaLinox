package com.linox.sistemaventas.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.linox.sistemaventas.models.Producto;

public interface ProductoRepository extends JpaRepository<Producto, Integer> {
    List<Producto> findByIdEstado(Integer estado);

    List<Producto> findByCodProductoAndIdEstado(String codProducto, Integer idEstado);

    @Query("SELECT p FROM Producto p WHERE p.stock <= 60 AND p.idEstado = 1")
    List<Producto> findProductosConBajoStock();

    int countByIdEstado(Integer estado);

    int countByStockLessThanAndIdEstado(Integer stock, Integer estado);
}
