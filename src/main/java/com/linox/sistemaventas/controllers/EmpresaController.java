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

import com.linox.sistemaventas.models.Empresa;
import com.linox.sistemaventas.models.EmpresaAnfitrion;
import com.linox.sistemaventas.services.EmpresaService;

@Controller
@RequestMapping("/empresa")
public class EmpresaController {

    @Autowired
    private EmpresaService empresaService;

    @GetMapping
    public String getAll(Model model) {
        List<Empresa> empresas = empresaService.listarActivas();
        model.addAttribute("empresas", empresas);
        model.addAttribute("active_page", "empresa");
        return "empresa/empresas";
    }

    @GetMapping("/create")
    public String crearEmpresa(Model model) {
        model.addAttribute("active_page", "empresa");
        return "empresa/crearEmpresa";
    }

    @PostMapping("/save")
    public String saveEmpresa(
            @RequestParam("ruc") String ruc,
            @RequestParam("razonSocial") String razonSocial,
            @RequestParam("nombreComercial") String nombreComercial,
            @RequestParam("direccion") String direccion,
            @RequestParam("telefono") String telefono,
            @RequestParam("correo") String correo,
            @RequestParam("idEstado") Integer idEstado,
            RedirectAttributes redirectAttributes) {
        try {
            EmpresaAnfitrion empresa = new EmpresaAnfitrion();
            empresa.setRuc(ruc);
            empresa.setRazonSocial(razonSocial);
            empresa.setNombreComercial(nombreComercial);
            empresa.setDireccion(direccion);
            empresa.setTelefono(telefono);
            empresa.setCorreo(correo);
            empresa.setIdEstado(idEstado);
            empresaService.guardar(empresa);
            redirectAttributes.addFlashAttribute("success", "Empresa guardada correctamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al guardar la empresa: " + e.getMessage());
        }
        return "redirect:/empresa";
    }

    @GetMapping("/editar/{id}")
    public String editarEmpresa(@PathVariable Integer id, Model model) {
        Optional<Empresa> empresaOpt = Optional.ofNullable(empresaService.buscarPorId(id));
        if (!empresaOpt.isPresent()) {
            return "redirect:/empresa";
        }
        model.addAttribute("empresa", empresaOpt.get());
        model.addAttribute("active_page", "empresa");
        return "empresa/editarEmpresa";
    }

    @PostMapping("/actualizar/{id}")
    public String actualizarEmpresa(
            @PathVariable Integer id,
            @RequestParam("ruc") String ruc,
            @RequestParam("razonSocial") String razonSocial,
            @RequestParam("nombreComercial") String nombreComercial,
            @RequestParam("direccion") String direccion,
            @RequestParam("telefono") String telefono,
            @RequestParam("correo") String correo,
            @RequestParam("idEstado") Integer idEstado,
            RedirectAttributes redirectAttributes) {
        try {
            Empresa empresa = empresaService.buscarPorId(id);
            if (empresa == null) {
                redirectAttributes.addFlashAttribute("error", "La empresa no fue encontrada.");
                return "redirect:/empresa";
            }

            empresa.setRuc(ruc);
            empresa.setRazonSocial(razonSocial);
            empresa.setNombreComercial(nombreComercial);
            empresa.setDireccion(direccion);
            empresa.setTelefono(telefono);
            empresa.setCorreo(correo);
            empresa.setIdEstado(idEstado);

            empresaService.actualizar(empresa);
            redirectAttributes.addFlashAttribute("success", "Empresa actualizada correctamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al actualizar la empresa: " + e.getMessage());
        }
        return "redirect:/empresa";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminarEmpresa(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            Empresa empresa = empresaService.buscarPorId(id);
            if (empresa == null) {
                redirectAttributes.addFlashAttribute("error", "La empresa no fue encontrada.");
                return "redirect:/empresa";
            }

            empresa.setIdEstado(0); // Estado inactivo
            empresaService.actualizar(empresa);
            redirectAttributes.addFlashAttribute("success", "Empresa eliminada correctamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar la empresa: " + e.getMessage());
        }
        return "redirect:/empresa";
    }
}
