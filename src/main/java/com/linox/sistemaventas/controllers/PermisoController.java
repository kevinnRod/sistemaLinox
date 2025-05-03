package com.linox.sistemaventas.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.linox.sistemaventas.models.Permiso;
import com.linox.sistemaventas.services.PermisoService;

@Controller
@RequestMapping("/permiso")
public class PermisoController {

    @Autowired
    private PermisoService permisoService;

    @GetMapping
    public String getAll(Model model) {
        List<Permiso> permisos = permisoService.findAllByEstadoActivo();
        model.addAttribute("permisos", permisos);
        model.addAttribute("active_page", "permiso");
        return "permiso/permisos";
    }

    @GetMapping("/create")
    public String crearPermiso(Model model) {
        model.addAttribute("active_page", "permiso");
        return "permiso/crearPermiso";
    }

    @PostMapping("/save")
    public String savePermiso(
            @RequestParam("nombrePermiso") String nombrePermiso,
            @RequestParam("descripcionPermiso") String descripcionPermiso,
            @RequestParam("idEstado") Integer idEstado,
            RedirectAttributes redirectAttributes) {
        try {
            Permiso permiso = new Permiso();
            permiso.setNombrePermiso(nombrePermiso);
            permiso.setDescripcionPermiso(descripcionPermiso);
            permiso.setIdEstado(idEstado);
            permisoService.save(permiso);
            redirectAttributes.addFlashAttribute("success", "Permiso guardado correctamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al guardar el permiso: " + e.getMessage());
        }
        return "redirect:/permiso";
    }

    @GetMapping("/editar/{id}")
    public String editarPermiso(@PathVariable Integer id, Model model) {
        Optional<Permiso> existente = permisoService.findById(id);
        if (!existente.isPresent()) {
            return "redirect:/permiso";
        }
        Permiso permisoExistente = existente.get();
        model.addAttribute("permiso", permisoExistente);
        model.addAttribute("active_page", "permiso");
        return "permiso/editarPermiso";
    }

    @PostMapping("/actualizar/{id}")
    public String actualizarPermiso(@PathVariable Integer id,
            @RequestParam("nombrePermiso") String nombrePermiso,
            @RequestParam("descripcionPermiso") String descripcionPermiso,
            @RequestParam("idEstado") Integer idEstado,
            RedirectAttributes redirectAttributes) {
        try {
            Optional<Permiso> permisoOpt = permisoService.findById(id);
            if (!permisoOpt.isPresent()) {
                redirectAttributes.addFlashAttribute("error", "El permiso no fue encontrado.");
                return "redirect:/permiso";
            }
            Permiso permiso = permisoOpt.get();
            permiso.setNombrePermiso(nombrePermiso);
            permiso.setDescripcionPermiso(descripcionPermiso);
            permiso.setIdEstado(idEstado);
            permisoService.save(permiso);
            redirectAttributes.addFlashAttribute("success", "Permiso actualizado correctamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al actualizar el permiso: " + e.getMessage());
        }
        return "redirect:/permiso";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminarPermiso(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            Optional<Permiso> permisoOpt = permisoService.findById(id);
            if (!permisoOpt.isPresent()) {
                redirectAttributes.addFlashAttribute("error", "El permiso no fue encontrado.");
                return "redirect:/permiso";
            }
            Permiso permiso = permisoOpt.get();
            permiso.setIdEstado(0); // Cambiar a inactivo
            permisoService.save(permiso);
            redirectAttributes.addFlashAttribute("success", "Permiso eliminado correctamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar el permiso: " + e.getMessage());
        }
        return "redirect:/permiso";
    }
}
