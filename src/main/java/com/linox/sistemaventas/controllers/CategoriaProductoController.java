package com.linox.sistemaventas.controllers;

import com.linox.sistemaventas.models.CategoriaProducto;
import com.linox.sistemaventas.services.CategoriaProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/categoriaProducto")
public class CategoriaProductoController {

    @Autowired
    private CategoriaProductoService categoriaProductoService;

    @GetMapping
    public String listarCategoriaProductos(Model model) {
        List<CategoriaProducto> categorias = categoriaProductoService.findAllActivos();
        model.addAttribute("categorias", categorias);
        model.addAttribute("active_page", "categoria-producto");
        return "categoria_producto/lista";
    }

    @GetMapping("/create")
    public String crearCategoriaProducto(Model model) {
        model.addAttribute("active_page", "categoria-producto");
        return "categoria_producto/crear";
    }

    @PostMapping("/save")
    public String guardarCategoriaProducto(
            @RequestParam("descripcion") String descripcion,
            @RequestParam("idEstado") Integer idEstado,
            RedirectAttributes redirectAttributes) {
        try {
            CategoriaProducto categoria = new CategoriaProducto();
            categoria.setDescripcion(descripcion);
            categoria.setIdEstado(idEstado);
            categoriaProductoService.save(categoria);
            redirectAttributes.addFlashAttribute("success", "Categoría de producto guardada correctamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al guardar la categoría: " + e.getMessage());
        }
        return "redirect:/categoriaProducto";
    }

    @GetMapping("/editar/{id}")
    public String editarCategoriaProducto(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        Optional<CategoriaProducto> categoriaOpt = categoriaProductoService.findById(id);
        if (!categoriaOpt.isPresent()) {
            redirectAttributes.addFlashAttribute("error", "Categoría no encontrada.");
            return "redirect:/categoriaProducto";
        }
        model.addAttribute("categoria", categoriaOpt.get());
        model.addAttribute("active_page", "categoria-producto");
        return "categoria_producto/editar";
    }

    @PostMapping("/actualizar/{id}")
    public String actualizarCategoriaProducto(
            @PathVariable Integer id,
            @RequestParam("descripcion") String descripcion,
            @RequestParam("idEstado") Integer idEstado,
            RedirectAttributes redirectAttributes) {
        try {
            Optional<CategoriaProducto> categoriaOpt = categoriaProductoService.findById(id);
            if (!categoriaOpt.isPresent()) {
                redirectAttributes.addFlashAttribute("error", "Categoría no encontrada.");
                return "redirect:/categoriaProducto";
            }
            CategoriaProducto categoria = categoriaOpt.get();
            categoria.setDescripcion(descripcion);
            categoria.setIdEstado(idEstado);
            categoriaProductoService.save(categoria);
            redirectAttributes.addFlashAttribute("success", "Categoría actualizada correctamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al actualizar la categoría: " + e.getMessage());
        }
        return "redirect:/categoriaProducto";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminarCategoriaProducto(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            Optional<CategoriaProducto> categoriaOpt = categoriaProductoService.findById(id);
            if (!categoriaOpt.isPresent()) {
                redirectAttributes.addFlashAttribute("error", "Categoría no encontrada.");
                return "redirect:/categoriaProducto";
            }
            CategoriaProducto categoria = categoriaOpt.get();
            categoria.setIdEstado(0); // Eliminación lógica
            categoriaProductoService.save(categoria);
            redirectAttributes.addFlashAttribute("success", "Categoría eliminada correctamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar la categoría: " + e.getMessage());
        }
        return "redirect:/categoriaProducto";
    }
}
