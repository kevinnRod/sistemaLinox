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

import com.linox.sistemaventas.models.Cargo;
import com.linox.sistemaventas.services.CargoService;

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
            RedirectAttributes redirectAttributes) {
        try {
            Optional<Cargo> cargoExistenteOpt = cargoService.buscarPorNombre(nombreCargo);

            if (cargoExistenteOpt.isPresent()) {
                Cargo cargoExistente = cargoExistenteOpt.get();
                if (cargoExistente.getIdEstado() == 0) {
                    // Si el cargo existe pero está inactivo, se actualiza y reactiva
                    cargoExistente.setIdEstado(1);
                    cargoService.guardar(cargoExistente);
                    redirectAttributes.addFlashAttribute("success", "Cargo reactivado correctamente.");
                    return "redirect:/cargo";
                } else {
                    // Ya existe y está activo
                    redirectAttributes.addFlashAttribute("error", "El nombre del cargo ya existe.");
                    return "redirect:/cargo/create";
                }
            }

            // Si no existe, se crea uno nuevo
            Cargo cargo = new Cargo();
            cargo.setNombreCargo(nombreCargo);
            cargo.setIdEstado(1);
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
            @RequestParam("idEstado") Integer idEstado,
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
            cargo.setIdEstado(0); // marcar como inactivo
            cargoService.guardar(cargo);
            redirectAttributes.addFlashAttribute("success", "Cargo eliminado correctamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar el cargo: " + e.getMessage());
        }
        return "redirect:/cargo";
    }
}
