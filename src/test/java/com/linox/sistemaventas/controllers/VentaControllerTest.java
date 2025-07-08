package com.linox.sistemaventas.controllers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.ui.Model;

import com.linox.sistemaventas.models.Cargo;
import com.linox.sistemaventas.models.CategoriaCliente;
import com.linox.sistemaventas.models.Cliente;
import com.linox.sistemaventas.models.ClienteNatural;
import com.linox.sistemaventas.models.Empleado;
import com.linox.sistemaventas.models.Persona;
import com.linox.sistemaventas.models.Sucursal;
import com.linox.sistemaventas.models.Venta;
import com.linox.sistemaventas.services.ClienteService;
import com.linox.sistemaventas.services.EmpleadoService;
import com.linox.sistemaventas.services.VentaService;

@SpringBootTest
@AutoConfigureMockMvc
public class VentaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private VentaController ventaController;

    @MockBean
    private VentaService ventaService;

    @MockBean
    private ClienteService clienteService;

    @MockBean
    private EmpleadoService empleadoService;

    @Test
    void testListarVentas() {
        // Arrange
        List<Venta> ventas = createSampleVentas();
        when(ventaService.findAllActiveVentas()).thenReturn(ventas);

        // Mock del clienteService para devolver un cliente ficticio
        when(clienteService.findById(anyString())).thenAnswer(invocation -> {
            String codCliente = invocation.getArgument(0);
            if ("CLI001".equals(codCliente)) {
                ClienteNatural cliente = new ClienteNatural();
                cliente.setCodCliente("CLI001");
                cliente.setIdEstado(1);
                Persona persona = new Persona();
                persona.setNombres("Carlos");
                persona.setApellidos("Gómez");
                persona.setDni("12345678");
                cliente.setPersona(persona);
                return java.util.Optional.of(cliente);
            }
            return java.util.Optional.empty();
        });

        Model model = mock(Model.class);

        // Act
        String viewName = ventaController.listarVentas(1, 8, model);

        // Assert
        assertEquals("venta/listar", viewName, "La vista devuelta no es la esperada.");
        verify(ventaService, times(1)).findAllActiveVentas();
        verify(clienteService, times(ventas.size())).findById(anyString());

        // Capturar los datos agregados al modelo
        ArgumentCaptor<List> ventasCaptor = ArgumentCaptor.forClass(List.class);
        verify(model, times(1)).addAttribute(eq("ventas"), ventasCaptor.capture());

        // Validar que las ventas capturadas sean del tipo esperado
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> ventasAgregadas = (List<Map<String, Object>>) ventasCaptor.getValue();
        assertNotNull(ventasAgregadas, "Las ventas no deberían ser nulas.");
        assertEquals(2, ventasAgregadas.size(), "El número de ventas no coincide.");

        // Validar los datos de la primera venta
        Map<String, Object> venta1 = ventasAgregadas.get(0);
        assertEquals("VENTA001", venta1.get("codigo"));
        assertEquals(1, venta1.get("id"));
        assertEquals(BigDecimal.valueOf(100.50), venta1.get("total"));
        assertEquals("Juan", venta1.get("empleado"));
        assertEquals("Carlos Gómez", venta1.get("nombre"));
        assertEquals("12345678", venta1.get("identificacion"));

        // Validar los datos de la segunda venta
        Map<String, Object> venta2 = ventasAgregadas.get(1);
        assertEquals("VENTA002", venta2.get("codigo"));
        assertEquals(2, venta2.get("id"));
        assertEquals(BigDecimal.valueOf(200.75), venta2.get("total"));
        assertEquals("Juan", venta2.get("empleado"));
        assertEquals("Carlos Gómez", venta2.get("nombre"));
        assertEquals("12345678", venta2.get("identificacion"));
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    void testGuardarVentaConDatosValidos() throws Exception {
        // Arrange
        ClienteNatural cliente = new ClienteNatural();
        cliente.setCodCliente("C001");
        cliente.setIdEstado(1);

        Empleado empleado = new Empleado();
        empleado.setIdPersona(1);
        empleado.setNombres("María Gómez");

        when(clienteService.findById("C001")).thenReturn(Optional.of(cliente));
        when(empleadoService.findById(1)).thenReturn(Optional.of(empleado));

        // Act & Assert
        mockMvc.perform(post("/ventas/guardar")
                .param("productoIds", "1", "2", "3")
                .param("cantidades", "2", "1", "3")
                .param("codCliente", "C001")
                .param("empleado.id", "1")
                .with(csrf()))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/ventas"));

        // Verifica que el método guardarVentaConDetalles fue llamado con los parámetros
        // correctos
        verify(ventaService).guardarVentaConDetalles(any(Venta.class), eq(Arrays.asList(1, 2, 3)),
                eq(Arrays.asList(2, 1, 3)));
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    void testGuardarVentaConClienteOEmpleadoInvalido() throws Exception {
        // Arrange
        when(clienteService.findById("C999")).thenReturn(Optional.empty());
        when(empleadoService.findById(999)).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(post("/ventas/guardar")
                .param("productoIds", "1", "2", "3")
                .param("cantidades", "2", "1", "3")
                .param("codCliente", "C999")
                .param("empleado.id", "999")
                .with(csrf()))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/ventas/crear"))
                .andExpect(flash().attributeExists("error"));

        // Verifica que el método guardarVentaConDetalles nunca fue llamado
        verify(ventaService, never()).guardarVentaConDetalles(any(), any(), any());
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    void testVerDetalleVentaNoExistente() throws Exception {
        // Arrange
        when(ventaService.findByCodVenta("V999")).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/ventas/V999"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/ventas?error=VentaNoEncontrada"));
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    void testVerDetalleVentaConClienteNoExistente() throws Exception {
        // Arrange
        Venta venta = new Venta();
        venta.setCodVenta("V003");
        Cliente cliente = new ClienteNatural();
        cliente.setCodCliente("C003");
        venta.setCliente(cliente);

        when(ventaService.findByCodVenta("V003")).thenReturn(Optional.of(venta));
        when(clienteService.findById("C003")).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/ventas/V003"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/ventas?error=ClienteNoEncontrado"));
    }

    private List<Venta> createSampleVentas() {
        // Crear un cargo ficticio
        Cargo cargo = new Cargo();
        cargo.setIdCargo(1);
        cargo.setNombreCargo("Vendedor");

        // Crear una sucursal ficticia
        Sucursal sucursal = new Sucursal();
        sucursal.setIdSucursal(1);
        sucursal.setNombreSucursal("Sucursal Central");

        // Crear un empleado ficticio
        Empleado empleado = new Empleado();
        empleado.setIdPersona(1);
        empleado.setCodEmpleado("EMP001");
        empleado.setNombres("Juan");
        empleado.setApellidos("Pérez");
        empleado.setCargo(cargo);
        empleado.setSucursal(sucursal);

        // Crear una categoría de cliente ficticia
        CategoriaCliente categoriaCliente = new CategoriaCliente();
        categoriaCliente.setIdCategoria(1);
        categoriaCliente.setNombre("Regular");

        // Crear un cliente ficticio
        ClienteNatural cliente = new ClienteNatural();
        cliente.setCodCliente("CLI001");
        cliente.setIdEstado(1);
        cliente.setCategoriaCliente(categoriaCliente);
        Persona persona = new Persona();
        persona.setIdPersona(2);
        persona.setNombres("Carlos");
        persona.setApellidos("Gómez");
        persona.setDni("12345678");
        cliente.setPersona(persona);

        // Crear ventas ficticias
        Venta venta1 = new Venta();
        venta1.setCodVenta("VENTA001");
        venta1.setIdVenta(1);
        venta1.setFechaV(LocalDateTime.now());
        venta1.setTotal(BigDecimal.valueOf(100.50));
        venta1.setEmpleado(empleado);
        venta1.setCliente(cliente);

        Venta venta2 = new Venta();
        venta2.setCodVenta("VENTA002");
        venta2.setIdVenta(2);
        venta2.setFechaV(LocalDateTime.now().minusDays(1));
        venta2.setTotal(BigDecimal.valueOf(200.75));
        venta2.setEmpleado(empleado);
        venta2.setCliente(cliente);

        return List.of(venta1, venta2);
    }

}
