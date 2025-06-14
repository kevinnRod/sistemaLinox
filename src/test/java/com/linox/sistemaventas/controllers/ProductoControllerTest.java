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
import com.linox.sistemaventas.models.Producto;
import com.linox.sistemaventas.services.ProductoService;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ProductoController.class)
@Import(SecurityConfigTest.class)
public class ProductoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductoService productoService;

    @Test
    public void eliminarLogicamente_productoExistente_redireccionExitosa() throws Exception {
        Producto producto = new Producto();
        producto.setId(1);
        producto.setIdEstado(1);

        when(productoService.findById(1)).thenReturn(Optional.of(producto));

        mockMvc.perform(MockMvcRequestBuilders.post("/productos/eliminar/1")
                .with(csrf()))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/productos"));

        // Verifica que se haya cambiado el estado y se haya guardado
        verify(productoService).save(any(Producto.class));
    }

    @Test
    public void eliminarLogicamente_productoNoExiste_redireccionSinGuardar() throws Exception {
        when(productoService.findById(999)).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.post("/productos/eliminar/999")
                .with(csrf()))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/productos"));

        // No se guarda nada si el producto no existe
        verify(productoService, never()).save(any(Producto.class));
    }

    @Test
    public void eliminarLogicamente_excepcion_redireccionConError() throws Exception {
        when(productoService.findById(anyInt())).thenThrow(new RuntimeException("Error inesperado"));

        mockMvc.perform(MockMvcRequestBuilders.post("/productos/eliminar/2")
                .with(csrf()))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/productos"));

        // No debe guardar nada si hay una excepción
        verify(productoService, never()).save(any(Producto.class));
    }
}
