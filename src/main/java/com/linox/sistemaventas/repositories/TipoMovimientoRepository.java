package com.linox.sistemaventas.repositories;

import com.linox.sistemaventas.models.TipoMovimiento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TipoMovimientoRepository extends JpaRepository<TipoMovimiento, Integer> {

    List<TipoMovimiento> findByIdEstado(Integer idEstado);

    Optional<TipoMovimiento> findByCodigo(String codigo);

    boolean existsByCodigo(String codigo);

    boolean existsByNombre(String nombre);
}
