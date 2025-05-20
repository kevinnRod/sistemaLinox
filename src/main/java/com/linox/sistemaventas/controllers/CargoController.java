package com.linox.sistemaventas.controllers;

import com.linox.sistemaventas.models.Cargo;
import com.linox.sistemaventas.services.CargoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/cargo")
public class CargoController {

    @Autowired
    private CargoService cargoService;

    @GetMapping
    public String listarCargos(Model model) {
        List<Cargo> cargos = cargoService.listar().stream()
                .filter(c -> c.getIdEstado() == 1)
                .toList();
        model.addAttribute("cargos", cargos);
        model.addAttribute("active_page", "cargo");
        return "cargo/cargos";
    }

    @GetMapping("/create")
    public String crearCargo(Model model) {
        model.addAttribute("active_page", "cargo");
        return "cargo/crearCargo";
    }

    @PostMapping("/save")
    public String guardarCargo(
            @RequestParam("nombreCargo") String nombreCargo,
            @RequestParam("idEstado") Short idEstado,
            RedirectAttributes redirectAttributes) {
        try {
            if (cargoService.existePorNombre(nombreCargo)) {
                redirectAttributes.addFlashAttribute("error", "El nombre del cargo ya existe.");
                return "redirect:/cargo/create";
            }

            Cargo cargo = new Cargo();
            cargo.setNombreCargo(nombreCargo);
            cargo.setIdEstado(idEstado);
            cargoService.guardar(cargo);
            redirectAttributes.addFlashAttribute("success", "Cargo guardado correctamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al guardar el cargo: " + e.getMessage());
        }
        return "redirect:/cargo";
    }

    @GetMapping("/editar/{id}")
    public String editarCargo(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Cargo> cargoOpt = cargoService.obtenerPorId(id);
        if (!cargoOpt.isPresent()) {
            redirectAttributes.addFlashAttribute("error", "El cargo no fue encontrado.");
            return "redirect:/cargo";
        }
        model.addAttribute("cargo", cargoOpt.get());
        model.addAttribute("active_page", "cargo");
        return "cargo/editarCargo";
    }

    @PostMapping("/actualizar/{id}")
    public String actualizarCargo(@PathVariable Integer id,
            @RequestParam("nombreCargo") String nombreCargo,
            @RequestParam("idEstado") Short idEstado,
            RedirectAttributes redirectAttributes) {
        try {
            Optional<Cargo> cargoOpt = cargoService.obtenerPorId(id);
            if (!cargoOpt.isPresent()) {
                redirectAttributes.addFlashAttribute("error", "El cargo no fue encontrado.");
                return "redirect:/cargo";
            }

            Cargo cargo = cargoOpt.get();
            cargo.setNombreCargo(nombreCargo);
            cargo.setIdEstado(idEstado);
            cargoService.guardar(cargo);
            redirectAttributes.addFlashAttribute("success", "Cargo actualizado correctamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al actualizar el cargo: " + e.getMessage());
        }
        return "redirect:/cargo";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminarCargo(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            Optional<Cargo> cargoOpt = cargoService.obtenerPorId(id);
            if (!cargoOpt.isPresent()) {
                redirectAttributes.addFlashAttribute("error", "El cargo no fue encontrado.");
                return "redirect:/cargo";
            }

            Cargo cargo = cargoOpt.get();
            cargo.setIdEstado((short) 0); // marcar como inactivo
            cargoService.guardar(cargo);
            redirectAttributes.addFlashAttribute("success", "Cargo eliminado correctamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar el cargo: " + e.getMessage());
        }
        return "redirect:/cargo";
    }
}
