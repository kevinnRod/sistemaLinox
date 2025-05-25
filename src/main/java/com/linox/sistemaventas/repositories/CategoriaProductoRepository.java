package com.linox.sistemaventas.repositories;

import com.linox.sistemaventas.models.CategoriaProducto;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CategoriaProductoRepository extends JpaRepository<CategoriaProducto, Integer> {
    List<CategoriaProducto> findByIdEstado(Integer estado);
}
