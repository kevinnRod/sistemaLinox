package com.linox.sistemaventas.controllers;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.linox.sistemaventas.models.TipoMovimiento;
import com.linox.sistemaventas.services.TipoMovimientoService;

@WebMvcTest(TipoMovimientoController.class)
@AutoConfigureMockMvc(addFilters = false) // <- Desactiva filtros de seguridad
class TipoMovimientoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TipoMovimientoService tipoMovimientoService;

    @Test
    @DisplayName("✅ Debe guardar el tipo de movimiento correctamente")
    void testGuardarTipoMovimiento_Success() throws Exception {
        TipoMovimiento tipo = new TipoMovimiento();
        tipo.setCodigo("Ingreso");
        tipo.setNombre("Descripción");
        when(tipoMovimientoService.save(any(TipoMovimiento.class)))
                .thenReturn(tipo);

        mockMvc.perform(post("/tipoMovimiento/save")
                .param("codigo", "Ingreso")
                .param("nombre", "Descripción"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/tipoMovimiento"));

        verify(tipoMovimientoService, times(1)).save(any(TipoMovimiento.class));
    }

    @Test
    @DisplayName("❌ Debe manejar el error al guardar tipo de movimiento")
    void testGuardarTipoMovimiento_Error() throws Exception {

        when(tipoMovimientoService.save(any(TipoMovimiento.class)))
                .thenThrow(new RuntimeException("Error al guardar"));

        mockMvc.perform(post("/tipoMovimiento/save")
                .param("codigo", "Ingreso")
                .param("nombre", "Descripción"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/tipoMovimiento"))
                .andExpect(flash().attributeExists("error"));

        verify(tipoMovimientoService, times(1)).save(any(TipoMovimiento.class));
    }

    @Test
    @DisplayName("⚠️ No debe guardar si los campos están vacíos")
    void testGuardarTipoMovimiento_CamposVacios() throws Exception {
        mockMvc.perform(post("/tipoMovimiento/save")
                .param("codigo", "")
                .param("nombre", ""))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/tipoMovimiento"))
                .andExpect(flash().attributeExists("error"));

        verify(tipoMovimientoService, never()).save(any(TipoMovimiento.class));
    }
}
