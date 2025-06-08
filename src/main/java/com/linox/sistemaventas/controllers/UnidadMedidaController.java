package com.linox.sistemaventas.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.linox.sistemaventas.models.UnidadMedida;
import com.linox.sistemaventas.services.UnidadMedidaService;

@Controller
@RequestMapping("/unidadMedida")
public class UnidadMedidaController {

    @Autowired
    private UnidadMedidaService unidadMedidaService;

    @GetMapping
    public String listarUnidadMedidas(Model model) {
        List<UnidadMedida> unidades = unidadMedidaService.findAllActivos();
        model.addAttribute("unidades", unidades);
        model.addAttribute("active_page", "unidad-medida");
        return "unidad_medida/lista";
    }

    @GetMapping("/create")
    public String crearUnidadMedida(Model model) {
        model.addAttribute("active_page", "unidad-medida");
        return "unidad_medida/crear";
    }

    @PostMapping("/save")
    public String guardarUnidadMedida(
            @RequestParam("descripcion") String descripcion,
            @RequestParam("simbolo") String simbolo,
            @RequestParam("idEstado") Integer idEstado,
            RedirectAttributes redirectAttributes) {
        try {
            boolean existeDescripcion = unidadMedidaService.existsByDescripcion(descripcion);
            boolean existeSimbolo = unidadMedidaService.existsBySimbolo(simbolo);

            if (existeDescripcion) {
                redirectAttributes.addFlashAttribute("error", "Ya existe una unidad con esa descripción.");
                return "redirect:/unidadMedida";
            }

            if (existeSimbolo) {
                redirectAttributes.addFlashAttribute("error", "Ya existe una unidad con ese símbolo.");
                return "redirect:/unidadMedida";
            }

            UnidadMedida unidad = new UnidadMedida();
            unidad.setDescripcion(descripcion);
            unidad.setSimbolo(simbolo);
            unidad.setIdEstado(idEstado);
            unidadMedidaService.save(unidad);

            redirectAttributes.addFlashAttribute("success", "Unidad de medida guardada correctamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al guardar la unidad: " + e.getMessage());
        }
        return "redirect:/unidadMedida";
    }

    @GetMapping("/editar/{id}")
    public String editarUnidadMedida(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        Optional<UnidadMedida> unidadOpt = unidadMedidaService.findById(id);
        if (!unidadOpt.isPresent()) {
            redirectAttributes.addFlashAttribute("error", "Unidad no encontrada.");
            return "redirect:/unidadMedida";
        }
        model.addAttribute("unidad", unidadOpt.get());
        model.addAttribute("active_page", "unidad-medida");
        return "unidad_medida/editar";
    }

    @PostMapping("/actualizar/{id}")
    public String actualizarUnidadMedida(
            @PathVariable Integer id,
            @RequestParam("descripcion") String descripcion,
            @RequestParam("simbolo") String simbolo,
            @RequestParam("idEstado") Integer idEstado,
            RedirectAttributes redirectAttributes) {
        try {
            Optional<UnidadMedida> unidadOpt = unidadMedidaService.findById(id);
            if (!unidadOpt.isPresent()) {
                redirectAttributes.addFlashAttribute("error", "Unidad no encontrada.");
                return "redirect:/unidadMedida";
            }

            boolean existeDescripcion = unidadMedidaService.existsByDescripcion(descripcion);
            boolean existeSimbolo = unidadMedidaService.existsBySimbolo(simbolo);

            if (existeDescripcion && !unidadOpt.get().getDescripcion().equals(descripcion)) {
                redirectAttributes.addFlashAttribute("error", "Ya existe una unidad con esa descripción.");
                return "redirect:/unidadMedida";
            }

            if (existeSimbolo && !unidadOpt.get().getSimbolo().equals(simbolo)) {
                redirectAttributes.addFlashAttribute("error", "Ya existe una unidad con ese símbolo.");
                return "redirect:/unidadMedida";
            }

            // Actualizar la unidad
            UnidadMedida unidad = unidadOpt.get();
            unidad.setDescripcion(descripcion);
            unidad.setSimbolo(simbolo);
            unidad.setIdEstado(idEstado);

            unidadMedidaService.save(unidad);
            redirectAttributes.addFlashAttribute("success", "Unidad actualizada correctamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al actualizar la unidad: " + e.getMessage());
        }

        return "redirect:/unidadMedida";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminarUnidadMedida(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            Optional<UnidadMedida> unidadOpt = unidadMedidaService.findById(id);
            if (!unidadOpt.isPresent()) {
                redirectAttributes.addFlashAttribute("error", "Unidad no encontrada.");
                return "redirect:/unidadMedida";
            }
            UnidadMedida unidad = unidadOpt.get();
            unidad.setIdEstado(0); // Eliminación lógica
            unidadMedidaService.save(unidad);
            redirectAttributes.addFlashAttribute("success", "Unidad eliminada correctamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar la unidad: " + e.getMessage());
        }
        return "redirect:/unidadMedida";
    }
}
