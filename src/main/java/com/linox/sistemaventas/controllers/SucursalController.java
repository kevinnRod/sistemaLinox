package com.linox.sistemaventas.controllers;

import com.linox.sistemaventas.models.Sucursal;
import com.linox.sistemaventas.models.EmpresaAnfitrion;
import com.linox.sistemaventas.services.SucursalService;
import com.linox.sistemaventas.services.EmpresaAnfitrionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/sucursal")
public class SucursalController {

    @Autowired
    private SucursalService sucursalService;

    @Autowired
    private EmpresaAnfitrionService empresaAnfitrionService;

    @GetMapping
    public String getAll(Model model) {
        List<Sucursal> sucursales = sucursalService.findAllByEstadoActivo();
        model.addAttribute("sucursales", sucursales);
        model.addAttribute("active_page", "sucursal");
        return "sucursal/sucursales";
    }

    @GetMapping("/create")
    public String crearSucursal(Model model) {
        List<EmpresaAnfitrion> empresas = empresaAnfitrionService.findAll();
        model.addAttribute("empresas", empresas);
        model.addAttribute("active_page", "sucursal");
        return "sucursal/crearSucursal";
    }

    @PostMapping("/save")
    public String saveSucursal(
            @RequestParam("nombreSucursal") String nombreSucursal,
            @RequestParam("direccion") String direccion,
            @RequestParam("telefono") String telefono,
            @RequestParam("email") String email,
            @RequestParam("idEmpresa") Integer idEmpresa,
            @RequestParam("idEstado") Integer idEstado,
            RedirectAttributes redirectAttributes) {
        try {
            Sucursal sucursal = new Sucursal();
            sucursal.setNombreSucursal(nombreSucursal);
            sucursal.setDireccion(direccion);
            sucursal.setTelefono(telefono);
            sucursal.setEmail(email);
            sucursal.setCreatedAt(LocalDateTime.now());
            sucursal.setUpdatedAt(LocalDateTime.now());
            sucursal.setIdEstado(idEstado);
            EmpresaAnfitrion empresa = empresaAnfitrionService.findById(idEmpresa).orElseThrow();
            sucursal.setEmpresaAnfitrion(empresa);
            sucursalService.save(sucursal);
            redirectAttributes.addFlashAttribute("success", "Sucursal guardada correctamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al guardar la sucursal: " + e.getMessage());
        }
        return "redirect:/sucursal";
    }

    @GetMapping("/editar/{id}")
    public String editarSucursal(@PathVariable Integer id, Model model) {
        Optional<Sucursal> sucursalOpt = sucursalService.findById(id);
        if (!sucursalOpt.isPresent()) {
            return "redirect:/sucursal";
        }
        List<EmpresaAnfitrion> empresas = empresaAnfitrionService.findAll();
        model.addAttribute("sucursal", sucursalOpt.get());
        model.addAttribute("empresas", empresas);
        model.addAttribute("active_page", "sucursal");
        return "sucursal/editarSucursal";
    }

    @PostMapping("/actualizar/{id}")
    public String actualizarSucursal(@PathVariable Integer id,
            @RequestParam("nombreSucursal") String nombreSucursal,
            @RequestParam("direccion") String direccion,
            @RequestParam("telefono") String telefono,
            @RequestParam("email") String email,
            @RequestParam("idEmpresa") Integer idEmpresa,
            @RequestParam("idEstado") Integer idEstado,
            RedirectAttributes redirectAttributes) {
        try {
            Optional<Sucursal> sucursalOpt = sucursalService.findById(id);
            if (!sucursalOpt.isPresent()) {
                redirectAttributes.addFlashAttribute("error", "La sucursal no fue encontrada.");
                return "redirect:/sucursal";
            }
            Sucursal sucursal = sucursalOpt.get();
            sucursal.setNombreSucursal(nombreSucursal);
            sucursal.setDireccion(direccion);
            sucursal.setTelefono(telefono);
            sucursal.setEmail(email);
            sucursal.setUpdatedAt(LocalDateTime.now());
            sucursal.setIdEstado(idEstado);
            EmpresaAnfitrion empresa = empresaAnfitrionService.findById(idEmpresa).orElseThrow();
            sucursal.setEmpresaAnfitrion(empresa);
            sucursalService.save(sucursal);
            redirectAttributes.addFlashAttribute("success", "Sucursal actualizada correctamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al actualizar la sucursal: " + e.getMessage());
        }
        return "redirect:/sucursal";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminarSucursal(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            Optional<Sucursal> sucursalOpt = sucursalService.findById(id);
            if (!sucursalOpt.isPresent()) {
                redirectAttributes.addFlashAttribute("error", "La sucursal no fue encontrada.");
                return "redirect:/sucursal";
            }
            Sucursal sucursal = sucursalOpt.get();
            sucursal.setIdEstado(0); // Marcar como inactiva
            sucursal.setUpdatedAt(LocalDateTime.now());
            sucursalService.save(sucursal);
            redirectAttributes.addFlashAttribute("success", "Sucursal eliminada correctamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar la sucursal: " + e.getMessage());
        }
        return "redirect:/sucursal";
    }
}
