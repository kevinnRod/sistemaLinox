package com.linox.sistemaventas.repositories;

import com.linox.sistemaventas.models.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;

public interface ProductoRepository extends JpaRepository<Producto, Integer> {
    List<Producto> findByIdEstado(Integer estado);
    List<Producto> findByCodProductoAndIdEstado(String codProducto, Integer idEstado);

}
