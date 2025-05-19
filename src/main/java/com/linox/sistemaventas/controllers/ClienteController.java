package com.linox.sistemaventas.controllers;

import com.linox.sistemaventas.models.*;
import com.linox.sistemaventas.services.CategoriaClienteService;
import com.linox.sistemaventas.services.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/cliente")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private CategoriaClienteService categoriaClienteService;

    // Listar clientes activos
    @GetMapping
    public String listarClientes(Model model) {
        List<Cliente> clientes = clienteService.findAllByEstadoActivo();
        model.addAttribute("clientes", clientes);
        model.addAttribute("active_page", "cliente");
        return "cliente/clientes";
    }

    // Mostrar formulario de creación
    @GetMapping("/create")
    public String crearCliente(Model model) {
        model.addAttribute("clienteNatural", new ClienteNatural());
        model.addAttribute("clienteJuridico", new ClienteJuridico());
        model.addAttribute("categorias", categoriaClienteService.findAllActivos());
        model.addAttribute("active_page", "cliente");
        return "cliente/crearCliente";
    }

    // Guardar cliente (natural o jurídico)
    @PostMapping("/save")
    public String guardarCliente(
            @RequestParam("tipo") String tipo,
            @RequestParam("nombres") String nombres,
            @RequestParam("apellidos") String apellidos,
            @RequestParam("dni") String dni,
            @RequestParam("telefono") String telefono,
            @RequestParam("direccion") String direccion,
            @RequestParam("idCategoriaCliente") Integer idCategoria,
            @RequestParam(value = "ruc", required = false) String ruc,
            RedirectAttributes redirectAttributes) {

        try {
            CategoriaCliente categoria = categoriaClienteService.findById(idCategoria)
                    .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

            Cliente cliente;
            if ("JURIDICO".equalsIgnoreCase(tipo)) {
                ClienteJuridico cj = new ClienteJuridico();
                cj.setRuc(ruc);
                cliente = cj;
            } else {
                cliente = new ClienteNatural();
            }

            cliente.setCodigoCliente(generarCodigoCliente());
            cliente.setNombres(nombres);
            cliente.setApellidos(apellidos);
            cliente.setDni(dni);
            cliente.setTelefono(telefono);
            cliente.setDireccion(direccion);
            cliente.setCategoriaCliente(categoria);
            cliente.setIdEstado(1);

            clienteService.save(cliente);
            redirectAttributes.addFlashAttribute("success", "Cliente registrado correctamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al registrar cliente: " + e.getMessage());
        }

        return "redirect:/cliente";
    }

    // Editar cliente
    @GetMapping("/editar/{id}")
    public String editarCliente(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Cliente> clienteOpt = clienteService.findById(id);
        if (!clienteOpt.isPresent()) {
            redirectAttributes.addFlashAttribute("error", "Cliente no encontrado.");
            return "redirect:/cliente";
        }

        Cliente cliente = clienteOpt.get();
        model.addAttribute("cliente", cliente);
        model.addAttribute("categorias", categoriaClienteService.findAllActivos());
        model.addAttribute("active_page", "cliente");

        return "cliente/editarCliente";
    }

    // Actualizar cliente
    @PostMapping("/actualizar/{id}")
    public String actualizarCliente(
            @PathVariable Integer id,
            @RequestParam("nombres") String nombres,
            @RequestParam("apellidos") String apellidos,
            @RequestParam("dni") String dni,
            @RequestParam("telefono") String telefono,
            @RequestParam("direccion") String direccion,
            @RequestParam("idCategoriaCliente") Integer idCategoria,
            @RequestParam(value = "ruc", required = false) String ruc,
            RedirectAttributes redirectAttributes) {

        try {
            Optional<Cliente> clienteOpt = clienteService.findById(id);
            if (!clienteOpt.isPresent()) {
                redirectAttributes.addFlashAttribute("error", "Cliente no encontrado.");
                return "redirect:/cliente";
            }

            Cliente cliente = clienteOpt.get();
            CategoriaCliente categoria = categoriaClienteService.findById(idCategoria)
                    .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

            cliente.setNombres(nombres);
            cliente.setApellidos(apellidos);
            cliente.setDni(dni);
            cliente.setTelefono(telefono);
            cliente.setDireccion(direccion);
            cliente.setCategoriaCliente(categoria);

            if (cliente instanceof ClienteJuridico && ruc != null) {
                ((ClienteJuridico) cliente).setRuc(ruc);
            }

            clienteService.save(cliente);
            redirectAttributes.addFlashAttribute("success", "Cliente actualizado correctamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al actualizar el cliente: " + e.getMessage());
        }

        return "redirect:/cliente";
    }

    // Eliminar cliente (lógico)
    @PostMapping("/eliminar/{id}")
    public String eliminarCliente(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            Optional<Cliente> clienteOpt = clienteService.findById(id);
            if (!clienteOpt.isPresent()) {
                redirectAttributes.addFlashAttribute("error", "Cliente no encontrado.");
                return "redirect:/cliente";
            }

            Cliente cliente = clienteOpt.get();
            cliente.setIdEstado(0); // Eliminación lógica
            clienteService.save(cliente);
            redirectAttributes.addFlashAttribute("success", "Cliente eliminado correctamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar el cliente: " + e.getMessage());
        }
        return "redirect:/cliente";
    }

    // Generador automático de código de cliente
    private String generarCodigoCliente() {
        long total = clienteService.countAll() + 1;
        return String.format("CLI-%05d", total);
    }
}
