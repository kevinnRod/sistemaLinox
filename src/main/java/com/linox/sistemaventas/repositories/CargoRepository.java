package com.linox.sistemaventas.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.linox.sistemaventas.models.Cargo;

@Repository
public interface CargoRepository extends JpaRepository<Cargo, Integer> {
    List<Cargo> findByIdEstado(Integer estado);

    boolean existsByNombreCargo(String nombreCargo);

    Optional<Cargo> findByNombreCargoIgnoreCase(String nombreCargo);

}
