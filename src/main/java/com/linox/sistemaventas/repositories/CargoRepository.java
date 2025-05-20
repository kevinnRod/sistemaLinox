package com.linox.sistemaventas.repositories;

import com.linox.sistemaventas.models.Cargo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CargoRepository extends JpaRepository<Cargo, Integer> {
    boolean existsByNombreCargo(String nombreCargo);
}
