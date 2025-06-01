package com.linox.sistemaventas.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.linox.sistemaventas.models.Sucursal;

@Repository
public interface SucursalRepository extends JpaRepository<Sucursal, Integer> {
    List<Sucursal> findByIdEstado(Integer idEstado);

    boolean existsByNombreSucursal(String nombreSucursal);

    Optional<Sucursal> findByNombreSucursal(String nombreSucursal);

}
