package com.linox.sistemaventas.repositories;

import com.linox.sistemaventas.models.Venta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VentaRepository extends JpaRepository<Venta, Integer> {
    List<Venta> findByIdEstado(Integer idEstado);
}
