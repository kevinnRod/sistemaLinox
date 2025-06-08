package com.linox.sistemaventas.controllers;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Optional;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.linox.sistemaventas.models.Cargo;
import com.linox.sistemaventas.models.Empleado;
import com.linox.sistemaventas.models.Sucursal;
import com.linox.sistemaventas.services.CargoService;
import com.linox.sistemaventas.services.EmpleadoService;
import com.linox.sistemaventas.services.SucursalService;

@WebMvcTest(EmpleadoController.class)
public class EmpleadoControllerTest {

        // Método que provee varios sets de datos para testear
        static Stream<Arguments> empleadosProvider() {
                return Stream.of(
                                Arguments.of("87654321", "Ana María", "García", "ana@gmail.com", "912345678",
                                                "Jr. Lima 101", 2, 1,
                                                "EMP00020"),
                                Arguments.of("23456789", "Carlos", "Ríos", "carlos.rios@mail.com", "987654321",
                                                "Av. Los Pinos", 1, 2,
                                                "EMP00021"),
                                Arguments.of("34567890", "Luis", "Pérez", "luis.perez@mail.com", "923456789", "Calle 3",
                                                1, 1,
                                                "EMP00022"),
                                Arguments.of("45678201", "Marta", "Lopez", "marta.lopez@mail.com", "934567890",
                                                "Av. Grau 404", 2, 2,
                                                "EMP00023"),
                                Arguments.of("56789012", "Josefina", "Morales", "josefina@mail.com", "945678901",
                                                "Jr. Amazonas 88", 1,
                                                1, "EMP00024"));
        }

        @Autowired
        private MockMvc mockMvc;

        @MockBean
        private EmpleadoService empleadoService;

        @MockBean
        private CargoService cargoService;

        @MockBean
        private SucursalService sucursalService;

        @ParameterizedTest
        @MethodSource("empleadosProvider")
        void testGuardarEmpleadoConDatosValidos(String dni, String nombres, String apellidos, String correo,
                        String telefono, String direccion, Integer idCargo, Integer idSucursal, String ultimoCod)
                        throws Exception {
                when(empleadoService.existsByDni(dni)).thenReturn(false);
                when(cargoService.findById(idCargo)).thenReturn(Optional.of(new Cargo()));
                when(sucursalService.findById(idSucursal)).thenReturn(Optional.of(new Sucursal()));
                when(empleadoService.obtenerUltimoCodigoEmpleado()).thenReturn(ultimoCod);

                mockMvc.perform(post("/empleado/save")
                                .param("dni", dni)
                                .param("nombres", nombres)
                                .param("apellidos", apellidos)
                                .param("correo", correo)
                                .param("telefono", telefono)
                                .param("direccion", direccion)
                                .param("idCargo", String.valueOf(idCargo))
                                .param("idSucursal", String.valueOf(idSucursal))
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                                .andExpect(status().is3xxRedirection())
                                .andExpect(redirectedUrl("/empleado"));

                verify(empleadoService).save(any(Empleado.class));
        }
}
