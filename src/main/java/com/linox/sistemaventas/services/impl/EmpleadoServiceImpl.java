package com.linox.sistemaventas.services.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.linox.sistemaventas.models.Empleado;
import com.linox.sistemaventas.repositories.EmpleadoRepository;
import com.linox.sistemaventas.services.EmpleadoService;

@Service
public class EmpleadoServiceImpl implements EmpleadoService {

    @Autowired
    private EmpleadoRepository empleadoRepository;

    @Override
    public List<Empleado> listarTodos() {
        return empleadoRepository.findAll();
    }

    @Override
    public Optional<Empleado> obtenerPorId(Integer id) {
        return empleadoRepository.findById(id);
    }

    @Override
    public Empleado save(Empleado empleado) {
        return empleadoRepository.save(empleado);
    }

    @Override
    public void eliminarPorId(Integer id) {
        empleadoRepository.deleteById(id);
    }

    @Override
    public List<Empleado> findAllByEstadoActivo() {
        return empleadoRepository.findByIdEstado(1); // Asegúrate de tener este método en el repository
    }

    @Override
    public Optional<Empleado> findById(Integer id) {
        return empleadoRepository.findById(id);
    }

    @Override
    public boolean existsByDni(String dni) {
        return empleadoRepository.existsByDni(dni);
    }

    @Override
    public String obtenerUltimoCodigoEmpleado() {
        Empleado ultimoEmpleado = empleadoRepository.findTopByOrderByCodEmpleadoDesc();
        if (ultimoEmpleado != null) {
            return ultimoEmpleado.getCodEmpleado(); // ej. EMP00023
        }
        return null; // si no hay ninguno
    }

}
