
package com.linox.sistemaventas.controllers;

import com.linox.sistemaventas.models.Venta;
import com.linox.sistemaventas.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Optional;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/ventas")
public class VentaController {

    @Autowired
    private VentaService ventaService;

    @Autowired
    private SucursalService sucursalService;

    @Autowired
    private PersonaService personaService;

    @Autowired
    private ClienteService clienteService;

    @GetMapping("/ventas")
    public List<Map<String, Object>> listarVentas() {
        return ventaService.findAllActivas().stream()
                .map(v -> {
                    Map<String, Object> datos = new HashMap<>();
                    datos.put("id", v.getIdVenta());
                    datos.put("numero", v.getCodVenta());
                    datos.put("cliente", v.getCliente().getNombres() + " " + v.getCliente().getApellidos());
                    datos.put("tipo", "VENTA");
                    return datos;
                })
                .toList();
    }

    // Mostrar formulario de creación
    @GetMapping("/create")
    public String mostrarFormularioCrear(Model model) {
        model.addAttribute("venta", new Venta());
        model.addAttribute("sucursales", sucursalService.findAllActivos());
        model.addAttribute("personas", personaService.findAllActivos());
        model.addAttribute("clientes", clienteService.findAllActivos());
        model.addAttribute("active_page", "ventas");
        return "ventas/crear";
    }

    // Guardar nueva venta
    @PostMapping("/save")
    public String guardar(@ModelAttribute Venta venta) {
        venta.setIdEstado(1);
        ventaService.save(venta);
        return "redirect:/ventas";
    }

    // Mostrar formulario de edición
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Integer id, Model model) {
        Optional<Venta> ventaOpt = ventaService.findById(id);
        if (ventaOpt.isPresent()) {
            model.addAttribute("venta", ventaOpt.get());
            model.addAttribute("sucursales", sucursalService.findAllActivos());
            model.addAttribute("personas", personaService.findAllActivos());
            model.addAttribute("clientes", clienteService.findAllActivos());
            model.addAttribute("active_page", "ventas");
            return "ventas/editar";
        }
        return "redirect:/ventas";
    }

    // Actualizar venta
    @PostMapping("/update")
    public String actualizar(@ModelAttribute Venta venta) {
        Optional<Venta> original = ventaService.findById(venta.getIdVenta());
        if (original.isPresent()) {
            venta.setCreatedAt(original.get().getCreatedAt());
            ventaService.save(venta);
        }
        return "redirect:/ventas";
    }

    // Eliminación lógica
    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id) {
        ventaService.deleteLogico(id);
        return "redirect:/ventas";
    }
}
