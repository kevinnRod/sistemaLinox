package com.linox.sistemaventas.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.linox.sistemaventas.models.Rol;
import com.linox.sistemaventas.models.Usuario;
import com.linox.sistemaventas.services.RolService;

@Controller
@RequestMapping("/rol")
public class RolController {

    @Autowired
    private RolService rolService;

    @GetMapping
    public String getAll(Model model) {
        List<Rol> roles = rolService.findAllByEstadoActivo();
        model.addAttribute("roles", roles); // Pasar la lista de roles a la vista
        model.addAttribute("active_page", "rol");
        return "rol/roles"; // Nombre de la vista Thymeleaf
    }

    @GetMapping("/create")
    public String crearRol(Model model) {
        model.addAttribute("active_page", "rol");
        return "rol/crearRol";
    }

    @PostMapping("/save")
    public String saveRol(
            @RequestParam("nombreRol") String nombreRol,
            @RequestParam("descripcionRol") String descripcionRol,
            @RequestParam("idEstado") Integer idEstado, RedirectAttributes redirectAttributes) {
        try {
            Rol rol = new Rol();
            rol.setNombreRol(nombreRol);
            rol.setDescripcionRol(descripcionRol);
            rol.setIdEstado(idEstado);
            rolService.save(rol);
            redirectAttributes.addFlashAttribute("success", "Rol guardado correctamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al guardar el rol: " + e.getMessage());
        }
        return "redirect:/rol";
    }

    @GetMapping("/editar/{id}")
    public String editarRol(@PathVariable Integer id, Model model) {
        Optional<Rol> existente = rolService.findById(id);
        if (!existente.isPresent()) {
            return "redirect:/rol";
        }
        Rol rolExistente = existente.get();
        model.addAttribute("rol", rolExistente);
        model.addAttribute("active_page", "rol");
        return "rol/editarRol"; // Vuelve a la vista usuario/crear.html
    }

    @PostMapping("/actualizar/{id}")
    public String actualizarRol(@PathVariable Integer id, RedirectAttributes redirectAttributes,
            @RequestParam("nombreRol") String nombreRol,
            @RequestParam("descripcionRol") String descripcionRol,
            @RequestParam("idEstado") Integer idEstado) {
        try {
            Optional<Rol> rolOpt = rolService.findById(id);

            if (!rolOpt.isPresent()) {
                redirectAttributes.addFlashAttribute("error", "El rol no fue encontrado.");
                return "redirect:/rol";
            }
            Rol rol = rolOpt.get();
            rol.setNombreRol(nombreRol);
            rol.setDescripcionRol(descripcionRol);
            rol.setIdEstado(idEstado);
            rolService.save(rol);
            redirectAttributes.addFlashAttribute("success", "Rol guardado correctamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al guardar el rol: " + e.getMessage());
        }
        return "redirect:/rol";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminarRol(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            Optional<Rol> rolOpt = rolService.findById(id);

            if (!rolOpt.isPresent()) {
                redirectAttributes.addFlashAttribute("error", "El rol no fue encontrado.");
                return "redirect:/rol";
            }

            Rol rol = rolOpt.get();
            rol.setIdEstado(0); // Cambia el estado a inactivo
            rolService.save(rol);

            redirectAttributes.addFlashAttribute("success", "Rol eliminado correctamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al guardar el rol: " + e.getMessage());
        }

        return "redirect:/rol";
    }
}