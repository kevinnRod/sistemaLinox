package com.linox.sistemaventas.controllers;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;

import java.time.LocalDateTime;
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
import com.linox.sistemaventas.models.Sucursal;
import com.linox.sistemaventas.services.SucursalService;

@ExtendWith(SpringExtension.class)
@WebMvcTest(SucursalController.class)
@Import(SecurityConfigTest.class)
public class SucursalControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SucursalService sucursalService;

    @MockBean
    private com.linox.sistemaventas.services.EmpresaAnfitrionService empresaAnfitrionService;

    @Test
    public void eliminarSucursal_existente_sucursalEliminada() throws Exception {
        Sucursal sucursal = new Sucursal();
        sucursal.setIdSucursal(1);
        sucursal.setNombreSucursal("Sucursal A");
        sucursal.setIdEstado(1);
        sucursal.setUpdatedAt(LocalDateTime.now());

        when(sucursalService.findById(1)).thenReturn(Optional.of(sucursal));

        mockMvc.perform(MockMvcRequestBuilders.post("/sucursal/eliminar/1").with(csrf()))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/sucursal"))
                .andExpect(MockMvcResultMatchers.flash().attributeExists("success"));
    }

    @Test
    public void eliminarSucursal_inexistente_redireccionConError() throws Exception {
        when(sucursalService.findById(999)).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.post("/sucursal/eliminar/999").with(csrf()))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/sucursal"))
                .andExpect(MockMvcResultMatchers.flash().attribute("error", "La sucursal no fue encontrada."));
    }

    @Test
    public void eliminarSucursal_exception_mensajeError() throws Exception {
        when(sucursalService.findById(anyInt())).thenThrow(new RuntimeException("Error inesperado"));

        mockMvc.perform(MockMvcRequestBuilders.post("/sucursal/eliminar/2").with(csrf()))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/sucursal"))
                .andExpect(MockMvcResultMatchers.flash().attributeExists("error"));
    }
}
