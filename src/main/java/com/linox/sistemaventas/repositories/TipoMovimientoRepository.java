package com.linox.sistemaventas.repositories;

import com.linox.sistemaventas.models.TipoMovimiento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TipoMovimientoRepository extends JpaRepository<TipoMovimiento, Integer> {

    List<TipoMovimiento> findByIdEstado(Integer idEstado);

    boolean existsByCodigo(String codigo);

    boolean existsByNombre(String nombre);
}
