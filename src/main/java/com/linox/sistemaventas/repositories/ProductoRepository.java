package com.linox.sistemaventas.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.linox.sistemaventas.models.Producto;
public interface ProductoRepository extends JpaRepository<Producto, Integer> {
    List<Producto> findByIdEstado(Integer estado);
    List<Producto> findByCodProductoAndIdEstado(String codProducto, Integer idEstado);

}
