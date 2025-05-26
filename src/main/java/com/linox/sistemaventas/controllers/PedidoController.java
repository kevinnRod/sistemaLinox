package com.linox.sistemaventas.controllers;

import com.linox.sistemaventas.models.Pedido;
import com.linox.sistemaventas.services.ClienteService;
import com.linox.sistemaventas.services.PedidoService;
import com.linox.sistemaventas.services.PersonaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/pedidos")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    @Autowired
    private PersonaService personaService;

    @Autowired
    private ClienteService clienteService;

    // Mostrar lista
    @GetMapping
    public String listarPedidos(Model model) {
        model.addAttribute("pedidos", pedidoService.findAllActivos());
        model.addAttribute("active_page", "pedidos");
        return "pedidos/lista";
    }

    // Mostrar formulario de creación
    @GetMapping("/create")
    public String mostrarFormularioCrear(Model model) {
        model.addAttribute("pedido", new Pedido());
        model.addAttribute("personas", personaService.findAllActivos());
        model.addAttribute("clientes", clienteService.findAllActivos());
        model.addAttribute("active_page", "pedidos");
        return "pedidos/crear";
    }

    // Guardar nuevo pedido
    @PostMapping("/save")
    public String guardar(@ModelAttribute Pedido pedido) {
        pedido.setEstado(1);
        pedidoService.save(pedido);
        return "redirect:/pedidos";
    }

    // Mostrar formulario de edición
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Integer id, Model model) {
        Optional<Pedido> pedidoOpt = pedidoService.findById(id);
        if (pedidoOpt.isPresent()) {
            model.addAttribute("pedido", pedidoOpt.get());
            model.addAttribute("personas", personaService.findAllActivos());
            model.addAttribute("clientes", clienteService.findAllActivos());
            model.addAttribute("active_page", "pedidos");
            return "pedidos/editar";
        }
        return "redirect:/pedidos";
    }

    // Actualizar pedido
    @PostMapping("/update")
    public String actualizar(@ModelAttribute Pedido pedido) {
        Optional<Pedido> original = pedidoService.findById(pedido.getIdPedido());
        if (original.isPresent()) {
            pedido.setFechPedido(original.get().getFechPedido()); // conservar la fecha de creación
            pedidoService.save(pedido);
        }
        return "redirect:/pedidos";
    }

    // Eliminación lógica
    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id) {
        pedidoService.deleteLogico(id);
        return "redirect:/pedidos";
    }
}
