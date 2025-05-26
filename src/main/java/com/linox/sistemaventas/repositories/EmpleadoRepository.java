package com.linox.sistemaventas.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.linox.sistemaventas.models.Empleado;

@Repository
public interface EmpleadoRepository extends JpaRepository<Empleado, Integer> {
    List<Empleado> findByIdEstado(Integer estado);

    boolean existsByDni(String dni);

    Empleado findTopByOrderByCodEmpleadoDesc();

    // Puedes agregar métodos personalizados si lo necesitas
}
