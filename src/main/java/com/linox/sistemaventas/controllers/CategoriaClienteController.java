package com.linox.sistemaventas.controllers;

import com.linox.sistemaventas.models.CategoriaCliente;
import com.linox.sistemaventas.services.CategoriaClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/categoriaCliente")
public class CategoriaClienteController {

    @Autowired
    private CategoriaClienteService categoriaClienteService;

    @GetMapping
    public String listarCategoriaClientes(Model model) {
        List<CategoriaCliente> categorias = categoriaClienteService.findAllActivos();
        model.addAttribute("categorias", categorias);
        model.addAttribute("active_page", "categoria-cliente");
        return "categoria_cliente/lista";
    }

    @GetMapping("/create")
    public String crearCategoriaCliente(Model model) {
        model.addAttribute("active_page", "categoria-cliente");
        return "categoria_cliente/crear";
    }

    @PostMapping("/save")
    public String guardarCategoriaCliente(
            @RequestParam("nombre") String nombre,
            @RequestParam("idEstado") Integer idEstado,
            RedirectAttributes redirectAttributes) {
        try {
            CategoriaCliente categoria = new CategoriaCliente();
            categoria.setNombre(nombre);
            categoria.setIdEstado(idEstado);
            categoriaClienteService.save(categoria);
            redirectAttributes.addFlashAttribute("success", "Categoría de cliente guardada correctamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al guardar la categoría: " + e.getMessage());
        }
        return "redirect:/categoriaCliente";
    }

    @GetMapping("/editar/{id}")
    public String editarCategoriaCliente(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        Optional<CategoriaCliente> categoriaOpt = categoriaClienteService.findById(id);
        if (!categoriaOpt.isPresent()) {
            redirectAttributes.addFlashAttribute("error", "Categoría no encontrada.");
            return "redirect:/categoriaCliente";
        }
        model.addAttribute("categoria", categoriaOpt.get());
        model.addAttribute("active_page", "categoria-cliente");
        return "categoria_cliente/editar";
    }

    @PostMapping("/actualizar/{id}")
    public String actualizarCategoriaCliente(
            @PathVariable Integer id,
            @RequestParam("nombre") String nombre,
            @RequestParam("idEstado") Integer idEstado,
            RedirectAttributes redirectAttributes) {
        try {
            Optional<CategoriaCliente> categoriaOpt = categoriaClienteService.findById(id);
            if (!categoriaOpt.isPresent()) {
                redirectAttributes.addFlashAttribute("error", "Categoría no encontrada.");
                return "redirect:/categoriaCliente";
            }
            CategoriaCliente categoria = categoriaOpt.get();
            categoria.setNombre(nombre);
            categoria.setIdEstado(idEstado);
            categoriaClienteService.save(categoria);
            redirectAttributes.addFlashAttribute("success", "Categoría actualizada correctamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al actualizar la categoría: " + e.getMessage());
        }
        return "redirect:/categoriaCliente";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminarCategoriaCliente(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            Optional<CategoriaCliente> categoriaOpt = categoriaClienteService.findById(id);
            if (!categoriaOpt.isPresent()) {
                redirectAttributes.addFlashAttribute("error", "Categoría no encontrada.");
                return "redirect:/categoriaCliente";
            }
            CategoriaCliente categoria = categoriaOpt.get();
            categoria.setIdEstado(0); // Eliminación lógica
            categoriaClienteService.save(categoria);
            redirectAttributes.addFlashAttribute("success", "Categoría eliminada correctamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar la categoría: " + e.getMessage());
        }
        return "redirect:/categoriaCliente";
    }
}
