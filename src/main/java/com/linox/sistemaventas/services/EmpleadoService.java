package com.linox.sistemaventas.services;

import com.linox.sistemaventas.models.Empleado;

import java.util.List;
import java.util.Optional;

public interface EmpleadoService {
    List<Empleado> listarTodos();

    List<Empleado> findAllByEstadoActivo();

    Optional<Empleado> obtenerPorId(Integer id);

    Empleado save(Empleado empleado);

    void eliminarPorId(Integer id);

    Optional<Empleado> findById(Integer id);

    boolean existsByDni(String dni);

    String obtenerUltimoCodigoEmpleado();

}
