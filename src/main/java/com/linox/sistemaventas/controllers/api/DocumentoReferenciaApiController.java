package com.linox.sistemaventas.controllers.api;

import com.linox.sistemaventas.services.PedidoService;
import com.linox.sistemaventas.services.VentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/documentos")
public class DocumentoReferenciaApiController {
    @Autowired
    private VentaService ventaService;

    @Autowired
    private PedidoService pedidoService;

    @GetMapping("/ventas")
    public List<Map<String, Object>> listarVentas() {
        return ventaService.findAllActivas().stream()
            .map(v -> {
                Map<String, Object> datos = new HashMap<>();
                datos.put("id", v.getIdVenta()); // ✅ correcto
                datos.put("numero", v.getCodVenta()); // ✅ correcto
                datos.put("cliente", v.getCliente().getNombres() + " " + v.getCliente().getApellidos()); // ✅ correcto si viene de Persona
                datos.put("tipo", "VENTA");
                return datos;
            })
            .toList(); // usa .collect(Collectors.toList()) si estás en Java 8
    }
    

    @GetMapping("/pedidos")
    public List<Map<String, Object>> listarPedidos() {
        return pedidoService.findAllActivos().stream()
            .map(p -> {
                Map<String, Object> datos = new HashMap<>();
                datos.put("id", p.getIdPedido()); // ✅ correcto
                datos.put("numero", "PED-" + p.getIdPedido()); // ✅ crea un código si no tienes campo separado
                datos.put("cliente", p.getCliente().getNombres() + " " + p.getCliente().getApellidos()); // ✅ desde Persona
                datos.put("tipo", "PEDIDO");
                return datos;
            })
            .toList(); // o .collect(Collectors.toList()) si estás en Java 8
    }

}
