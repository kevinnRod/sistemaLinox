package com.linox.sistemaventas.service;

import com.linox.sistemaventas.services.impl.EmpleadoServiceImpl;

import com.linox.sistemaventas.models.Cargo;
import com.linox.sistemaventas.models.Empleado;
import com.linox.sistemaventas.models.Sucursal;
import com.linox.sistemaventas.repositories.EmpleadoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class EmpleadoServiceImplTest {

    @Mock
    private EmpleadoRepository empleadoRepository;

    @InjectMocks
    private EmpleadoServiceImpl empleadoService;

    private Empleado empleado;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        Cargo cargo = new Cargo();
        Sucursal sucursal = new Sucursal();

        empleado = new Empleado();
        empleado.setIdPersona(1);
        empleado.setCodEmpleado("EMP00001");
        empleado.setDni("12345678");
        empleado.setCargo(cargo);
        empleado.setSucursal(sucursal);
    }

    @Test
    void testListarTodos() {
        List<Empleado> lista = Arrays.asList(empleado);
        when(empleadoRepository.findAll()).thenReturn(lista);

        List<Empleado> resultado = empleadoService.listarTodos();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(empleadoRepository, times(1)).findAll();
    }

    @Test
    void testGuardarEmpleado() {
        when(empleadoRepository.save(any(Empleado.class))).thenReturn(empleado);

        Empleado guardado = empleadoService.save(empleado);

        assertNotNull(guardado);
        assertEquals("EMP00001", guardado.getCodEmpleado());
        verify(empleadoRepository).save(empleado);
    }

    @Test
    void testEliminarPorId() {
        Integer id = 1;
        doNothing().when(empleadoRepository).deleteById(id);

        empleadoService.eliminarPorId(id);

        verify(empleadoRepository, times(1)).deleteById(id);
    }

    @Test
    void testExistePorDni() {
        when(empleadoRepository.existsByDni("12345678")).thenReturn(true);

        boolean existe = empleadoService.existsByDni("12345678");

        assertTrue(existe);
        verify(empleadoRepository).existsByDni("12345678");
    }

    @Test
    void testObtenerUltimoCodigoEmpleado() {
        when(empleadoRepository.findTopByOrderByCodEmpleadoDesc()).thenReturn(empleado);

        String ultimoCodigo = empleadoService.obtenerUltimoCodigoEmpleado();

        assertEquals("EMP00001", ultimoCodigo);
    }

    @Test
    void testObtenerUltimoCodigoEmpleado_SinEmpleados() {
        when(empleadoRepository.findTopByOrderByCodEmpleadoDesc()).thenReturn(null);

        String ultimoCodigo = empleadoService.obtenerUltimoCodigoEmpleado();

        assertNull(ultimoCodigo);
    }
}
