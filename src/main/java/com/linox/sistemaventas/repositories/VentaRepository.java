package com.linox.sistemaventas.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.linox.sistemaventas.models.Venta;

public interface VentaRepository extends JpaRepository<Venta, Integer> {
    List<Venta> findByIdEstado(Integer idEstado);

    Optional<Venta> findByCodVenta(String codVenta);

}
