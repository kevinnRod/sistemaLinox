package com.linox.sistemaventas.controllers;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.linox.sistemaventas.config.SecurityConfigTest;
import com.linox.sistemaventas.models.Cargo;
import com.linox.sistemaventas.models.Empleado;
import com.linox.sistemaventas.models.Sucursal;
import com.linox.sistemaventas.services.CargoService;
import com.linox.sistemaventas.services.EmpleadoService;
import com.linox.sistemaventas.services.SucursalService;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = EmpleadoController.class)
@Import(SecurityConfigTest.class)
class EmpleadoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmpleadoService empleadoService;

    @MockBean
    private CargoService cargoService;

    @MockBean
    private SucursalService sucursalService;

    @InjectMocks
    private EmpleadoController empleadoController;

    private Cargo cargoMock;
    private Sucursal sucursalMock;

    @BeforeEach
    void setUp() {
        cargoMock = new Cargo();
        cargoMock.setIdCargo(1);
        cargoMock.setNombreCargo("Administrador");

        sucursalMock = new Sucursal();
        sucursalMock.setIdSucursal(1);
        sucursalMock.setNombreSucursal("Principal");
    }

    @Test
    void guardarEmpleadoConDatosValidos_deberiaRedirigirConExito() throws Exception {
        when(empleadoService.existsByDni("12345678")).thenReturn(false);
        when(cargoService.findById(1)).thenReturn(Optional.of(cargoMock));
        when(sucursalService.findById(1)).thenReturn(Optional.of(sucursalMock));

        mockMvc.perform(post("/empleado/save")
                .param("dni", "12345678")
                .param("nombres", "Juan")
                .param("apellidos", "Pérez")
                .param("correo", "juan@example.com")
                .param("telefono", "987654321")
                .param("direccion", "Av. Siempre Viva 742")
                .param("idCargo", "1")
                .param("idSucursal", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/empleado"))
                .andExpect(flash().attribute("success", "Empleado guardado correctamente."));

        verify(empleadoService).save(any(Empleado.class));
    }

    @Test
    void guardarEmpleadoConDniExistente_deberiaMostrarError() throws Exception {
        when(empleadoService.existsByDni("12345678")).thenReturn(true);

        mockMvc.perform(post("/empleado/save")
                .param("dni", "12345678")
                .param("nombres", "Juan")
                .param("apellidos", "Pérez")
                .param("correo", "juan@example.com")
                .param("telefono", "987654321")
                .param("direccion", "Av. Siempre Viva 742")
                .param("idCargo", "1")
                .param("idSucursal", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/empleado/create"))
                .andExpect(flash().attributeExists("errores"))
                .andExpect(flash().attributeExists("datos"));
    }

    @Test
    void guardarEmpleadoConCorreoInvalido_deberiaMostrarError() throws Exception {
        mockMvc.perform(post("/empleado/save")
                .param("dni", "12345678")
                .param("nombres", "Juan")
                .param("apellidos", "Pérez")
                .param("correo", "correo-invalido")
                .param("telefono", "987654321")
                .param("direccion", "Av. Siempre Viva 742")
                .param("idCargo", "1")
                .param("idSucursal", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/empleado/create"))
                .andExpect(flash().attributeExists("errores"))
                .andExpect(flash().attributeExists("datos"));
    }

    @Test
    void guardarEmpleadoConCamposVacios_deberiaMostrarErrores() throws Exception {
        mockMvc.perform(post("/empleado/save")
                .param("dni", "")
                .param("nombres", "")
                .param("apellidos", "")
                .param("correo", "")
                .param("telefono", "")
                .param("direccion", "")
                .param("idCargo", "1")
                .param("idSucursal", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/empleado/create"))
                .andExpect(flash().attributeExists("errores"))
                .andExpect(flash().attributeExists("datos"));
    }
}