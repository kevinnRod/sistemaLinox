package com.linox.sistemaventas.controllers;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.linox.sistemaventas.config.SecurityConfigTest;
import com.linox.sistemaventas.models.Cargo;
import com.linox.sistemaventas.services.CargoService;

@ExtendWith(SpringExtension.class)
@WebMvcTest(CargoController.class)
@Import(SecurityConfigTest.class)
public class CargoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CargoService cargoService;

    @Test
    public void actualizarCargo_existente_actualizadoCorrectamente() throws Exception {
        Cargo cargo = new Cargo();
        cargo.setIdCargo(1);
        cargo.setNombreCargo("Cargo A");
        cargo.setIdEstado(1);

        when(cargoService.obtenerPorId(1)).thenReturn(Optional.of(cargo));

        mockMvc.perform(MockMvcRequestBuilders.post("/cargo/actualizar/1")
                .param("nombreCargo", "Nuevo Cargo A")
                .param("idEstado", "2")
                .with(csrf()))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/cargo"))
                .andExpect(MockMvcResultMatchers.flash().attributeExists("success"));

        verify(cargoService).guardar(any(Cargo.class));
    }

    @Test
    public void actualizarCargo_inexistente_redireccionConError() throws Exception {
        when(cargoService.obtenerPorId(999)).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.post("/cargo/actualizar/999")
                .param("nombreCargo", "Nuevo Nombre")
                .param("idEstado", "1")
                .with(csrf()))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/cargo"))
                .andExpect(MockMvcResultMatchers.flash().attribute("error", "El cargo no fue encontrado."));
    }

    @Test
    public void actualizarCargo_exception_redireccionConMensajeError() throws Exception {
        when(cargoService.obtenerPorId(anyInt())).thenThrow(new RuntimeException("Error inesperado"));

        mockMvc.perform(MockMvcRequestBuilders.post("/cargo/actualizar/2")
                .param("nombreCargo", "Error Cargo")
                .param("idEstado", "1")
                .with(csrf()))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/cargo"))
                .andExpect(MockMvcResultMatchers.flash().attributeExists("error"));
    }
}
