package com.linox.sistemaventas.controllers;

import java.time.LocalDateTime;
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

import com.linox.sistemaventas.models.EmpresaAnfitrion;
import com.linox.sistemaventas.models.Sucursal;
import com.linox.sistemaventas.services.EmpresaAnfitrionService;
import com.linox.sistemaventas.services.SucursalService;

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
            RedirectAttributes redirectAttributes) {
        try {
            // Buscar si la sucursal existe sin importar estado
            Optional<Sucursal> sucursalExistenteOpt = sucursalService.findByNombreSucursal(nombreSucursal);

            if (sucursalExistenteOpt.isPresent()) {
                Sucursal sucursalExistente = sucursalExistenteOpt.get();
                if (sucursalExistente.getIdEstado() == 0) {
                    // Si existe y está inactiva, actualizar sus datos y reactivar
                    sucursalExistente.setDireccion(direccion);
                    sucursalExistente.setTelefono(telefono);
                    sucursalExistente.setEmail(email);
                    sucursalExistente.setUpdatedAt(LocalDateTime.now());
                    sucursalExistente.setIdEstado(1);
                    EmpresaAnfitrion empresa = empresaAnfitrionService.findById(idEmpresa).orElseThrow();
                    sucursalExistente.setEmpresaAnfitrion(empresa);
                    sucursalService.save(sucursalExistente);
                    redirectAttributes.addFlashAttribute("success", "Sucursal reactivada y actualizada correctamente.");
                    return "redirect:/sucursal";
                } else {
                    // Ya existe y está activa
                    redirectAttributes.addFlashAttribute("error", "El nombre de la sucursal ya existe.");
                    return "redirect:/sucursal/create";
                }
            }

            // No existe, se crea una nueva
            Sucursal nuevaSucursal = new Sucursal();
            nuevaSucursal.setNombreSucursal(nombreSucursal);
            nuevaSucursal.setDireccion(direccion);
            nuevaSucursal.setTelefono(telefono);
            nuevaSucursal.setEmail(email);
            nuevaSucursal.setCreatedAt(LocalDateTime.now());
            nuevaSucursal.setUpdatedAt(LocalDateTime.now());
            nuevaSucursal.setIdEstado(1);
            EmpresaAnfitrion empresa = empresaAnfitrionService.findById(idEmpresa).orElseThrow();
            nuevaSucursal.setEmpresaAnfitrion(empresa);
            sucursalService.save(nuevaSucursal);
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

            // Validar si el nombre ya existe en otra sucursal
            Optional<Sucursal> existente = sucursalService.findByNombreSucursal(nombreSucursal);
            if (existente.get() != null && !existente.get().getIdSucursal().equals(id)) {
                redirectAttributes.addFlashAttribute("error", "El nombre de la sucursal ya está en uso.");
                return "redirect:/sucursal/editar/" + id;
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
    public String eliminarSucursal(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        try {
            Optional<Sucursal> sucursalOpt = sucursalService.findById(id);
            if (sucursalOpt.isPresent()) {
                Sucursal sucursal = sucursalOpt.get();
                sucursal.setIdEstado(0); // Baja lógica
                sucursal.setUpdatedAt(LocalDateTime.now());
                sucursalService.save(sucursal);
                redirectAttributes.addFlashAttribute("success", "Sucursal eliminada correctamente.");
            } else {
                redirectAttributes.addFlashAttribute("error", "La sucursal no fue encontrada.");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ocurrió un error al eliminar la sucursal.");
        }
        return "redirect:/sucursal";
    }
}
