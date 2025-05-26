package com.linox.sistemaventas.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.linox.sistemaventas.models.Sucursal;

@Repository
public interface SucursalRepository extends JpaRepository<Sucursal, Integer> {
    List<Sucursal> findByIdEstado(Integer idEstado);
}
