package com.linox.sistemaventas.controllers;

import com.linox.sistemaventas.models.Kardex;
import com.linox.sistemaventas.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/kardex")
public class KardexController {

    @Autowired
    private KardexService kardexService;

    @Autowired
    private ProductoService productoService;

    @Autowired
    private TipoMovimientoService tipoMovimientoService;

    @Autowired
    private SucursalService sucursalService;
    @Autowired
    private VentaService ventaService;
    @Autowired
    private PedidoService pedidoService;

    // Mostrar lista de movimientos
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("kardex", kardexService.findAllActivos());
        model.addAttribute("active_page", "kardex");
        return "kardex/lista";
    }

    // Mostrar formulario de creación
    @GetMapping("/create")
    public String mostrarFormularioCrear(Model model) {
        model.addAttribute("kardex", new Kardex());
        model.addAttribute("productos", productoService.findAllActivos());
        model.addAttribute("tiposMovimiento", tipoMovimientoService.findAllActivos());
        model.addAttribute("sucursales", sucursalService.findAllActivos());
        model.addAttribute("active_page", "kardex");
        model.addAttribute("ventas", ventaService.findAllActiveVentas());
        model.addAttribute("pedidos", pedidoService.findAllActivos());

        return "kardex/crear";
    }

    // Guardar nuevo movimiento
    @PostMapping("/save")
    public String guardar(@ModelAttribute Kardex kardex) {
        kardex.setIdEstado(1);
        kardexService.save(kardex);
        return "redirect:/kardex";
    }

    // Mostrar formulario de edición
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Integer id, Model model) {
        Optional<Kardex> kardexOpt = kardexService.findById(id);
        if (kardexOpt.isPresent()) {
            model.addAttribute("kardex", kardexOpt.get());
            model.addAttribute("productos", productoService.findAllActivos());
            model.addAttribute("tiposMovimiento", tipoMovimientoService.findAllActivos());
            model.addAttribute("sucursales", sucursalService.findAllActivos());
            model.addAttribute("active_page", "kardex");
            return "kardex/editar";
        } else {
            return "redirect:/kardex";
        }
    }

    // Actualizar movimiento
    @PostMapping("/update")
    public String actualizar(@ModelAttribute Kardex kardex) {
        Optional<Kardex> originalOpt = kardexService.findById(kardex.getIdKardex());
        if (originalOpt.isPresent()) {
            kardex.setCreatedAt(originalOpt.get().getCreatedAt());
            kardexService.save(kardex);
        }
        return "redirect:/kardex";
    }

    // Eliminación lógica
    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id) {
        kardexService.deleteLogico(id);
        return "redirect:/kardex";
    }
}
