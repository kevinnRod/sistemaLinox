package com.linox.sistemaventas.controllers;

import com.linox.sistemaventas.models.TipoMovimiento;
import com.linox.sistemaventas.services.TipoMovimientoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/tipoMovimiento")
public class TipoMovimientoController {

    @Autowired
    private TipoMovimientoService tipoMovimientoService;

    @GetMapping
    public String listarTiposMovimiento(Model model) {
        List<TipoMovimiento> tipos = tipoMovimientoService.findAllActivos();
        model.addAttribute("tipos", tipos);
        model.addAttribute("active_page", "tipo-movimiento");
        return "tipo_movimiento/lista";
    }

    @GetMapping("/create")
    public String crearTipoMovimiento(Model model) {
        model.addAttribute("active_page", "tipo-movimiento");
        return "tipo_movimiento/crear";
    }

    @PostMapping("/save")
    public String guardarTipoMovimiento(
            @RequestParam String codigo,
            @RequestParam String nombre,
            RedirectAttributes redirectAttributes) {

        try {
            TipoMovimiento tipo = new TipoMovimiento();
            tipo.setCodigo(codigo);
            tipo.setNombre(nombre);
            tipo.setIdEstado(1);
            tipoMovimientoService.save(tipo);
            redirectAttributes.addFlashAttribute("success", "Tipo de movimiento registrado correctamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al registrar: " + e.getMessage());
        }

        return "redirect:/tipoMovimiento";
    }

    @GetMapping("/editar/{id}")
    public String editarTipoMovimiento(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        Optional<TipoMovimiento> tipoOpt = tipoMovimientoService.findById(id);
        if (!tipoOpt.isPresent()) {
            redirectAttributes.addFlashAttribute("error", "Tipo de movimiento no encontrado.");
            return "redirect:/tipoMovimiento";
        }

        model.addAttribute("tipo", tipoOpt.get());
        model.addAttribute("active_page", "tipo-movimiento");
        return "tipo_movimiento/editar";
    }

    @PostMapping("/actualizar/{id}")
    public String actualizarTipoMovimiento(
            @PathVariable Integer id,
            @RequestParam String codigo,
            @RequestParam String nombre,
            @RequestParam Integer idEstado,
            RedirectAttributes redirectAttributes) {

        try {
            Optional<TipoMovimiento> tipoOpt = tipoMovimientoService.findById(id);
            if (!tipoOpt.isPresent()) {
                redirectAttributes.addFlashAttribute("error", "Tipo de movimiento no encontrado.");
                return "redirect:/tipoMovimiento";
            }

            TipoMovimiento tipo = tipoOpt.get();
            tipo.setCodigo(codigo);
            tipo.setNombre(nombre);
            tipo.setIdEstado(idEstado);
            tipoMovimientoService.save(tipo);

            redirectAttributes.addFlashAttribute("success", "Tipo de movimiento actualizado correctamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al actualizar: " + e.getMessage());
        }

        return "redirect:/tipoMovimiento";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminarTipoMovimiento(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            Optional<TipoMovimiento> tipoOpt = tipoMovimientoService.findById(id);
            if (!tipoOpt.isPresent()) {
                redirectAttributes.addFlashAttribute("error", "Tipo de movimiento no encontrado.");
                return "redirect:/tipoMovimiento";
            }

            TipoMovimiento tipo = tipoOpt.get();
            tipo.setIdEstado(0); // Eliminación lógica
            tipoMovimientoService.save(tipo);

            redirectAttributes.addFlashAttribute("success", "Tipo de movimiento eliminado correctamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar: " + e.getMessage());
        }

        return "redirect:/tipoMovimiento";
    }
}
