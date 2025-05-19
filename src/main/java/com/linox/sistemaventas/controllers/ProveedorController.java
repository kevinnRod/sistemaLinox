package com.linox.sistemaventas.controllers;

import com.linox.sistemaventas.models.Empresa;
import com.linox.sistemaventas.models.Proveedor;
import com.linox.sistemaventas.services.EmpresaService;
import com.linox.sistemaventas.services.ProveedorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/proveedor")
public class ProveedorController {

    @Autowired
    private ProveedorService proveedorService;

    @Autowired
    private EmpresaService empresaService;

    @GetMapping
    public String listarProveedores(Model model) {
        List<Proveedor> proveedores = proveedorService.findAllActivos();
        model.addAttribute("proveedores", proveedores);
        model.addAttribute("active_page", "proveedor");
        return "proveedor/lista";
    }

    @GetMapping("/create")
    public String crearProveedor(Model model) {
        List<Empresa> empresas = empresaService.findAll(); // Para seleccionar empresa
        model.addAttribute("empresas", empresas);
        model.addAttribute("active_page", "proveedor");
        return "proveedor/crear";
    }

    @PostMapping("/save")
public String guardarProveedor(
        @RequestParam String ruc,
        @RequestParam String razonSocial,
        @RequestParam(required = false) String nombreComercial,
        @RequestParam(required = false) String direccion,
        @RequestParam(required = false) String telefono,
        @RequestParam(required = false) String correo,
        RedirectAttributes redirectAttributes) {

    // Validaciones
    if (!ruc.matches("\\d{11}")) {
        redirectAttributes.addFlashAttribute("error", "El RUC debe tener exactamente 11 dígitos.");
        redirectAttributes.addFlashAttribute("ruc", ruc);
        redirectAttributes.addFlashAttribute("razonSocial", razonSocial);
        redirectAttributes.addFlashAttribute("nombreComercial", nombreComercial);
        redirectAttributes.addFlashAttribute("direccion", direccion);
        redirectAttributes.addFlashAttribute("telefono", telefono);
        redirectAttributes.addFlashAttribute("correo", correo);
        return "redirect:/proveedor/create";
    }

    if (empresaService.buscarPorRuc(ruc).isPresent()) {
        redirectAttributes.addFlashAttribute("error", "Ya existe una empresa registrada con ese RUC.");
        redirectAttributes.addFlashAttribute("ruc", ruc);
        redirectAttributes.addFlashAttribute("razonSocial", razonSocial);
        redirectAttributes.addFlashAttribute("nombreComercial", nombreComercial);
        redirectAttributes.addFlashAttribute("direccion", direccion);
        redirectAttributes.addFlashAttribute("telefono", telefono);
        redirectAttributes.addFlashAttribute("correo", correo);
        return "redirect:/proveedor/create";
    }

    if (empresaService.buscarPorRazonSocial(razonSocial).isPresent()) {
        redirectAttributes.addFlashAttribute("error", "Ya existe una empresa registrada con esa Razón Social.");
        redirectAttributes.addFlashAttribute("ruc", ruc);
        redirectAttributes.addFlashAttribute("razonSocial", razonSocial);
        redirectAttributes.addFlashAttribute("nombreComercial", nombreComercial);
        redirectAttributes.addFlashAttribute("direccion", direccion);
        redirectAttributes.addFlashAttribute("telefono", telefono);
        redirectAttributes.addFlashAttribute("correo", correo);
        return "redirect:/proveedor/create";
    }

    try {
        Empresa empresa = new Empresa();
        empresa.setRuc(ruc);
        empresa.setRazonSocial(razonSocial);
        empresa.setNombreComercial(nombreComercial);
        empresa.setDireccion(direccion);
        empresa.setTelefono(telefono);
        empresa.setCorreo(correo);
        empresa.setIdEstado(1);
        empresa = empresaService.guardar(empresa);

        Proveedor proveedor = new Proveedor();
        proveedor.setEmpresa(empresa);
        proveedor.setIdEstado(1);
        proveedorService.save(proveedor);

        redirectAttributes.addFlashAttribute("success", "Proveedor y empresa registrados correctamente.");
        return "redirect:/proveedor";
    } catch (Exception e) {
        redirectAttributes.addFlashAttribute("error", "Error al registrar: " + e.getMessage());
        return "redirect:/proveedor/create";
    }
}

    
    



    @GetMapping("/editar/{id}")
    public String editarProveedor(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Proveedor> proveedorOpt = proveedorService.findById(id);
        if (!proveedorOpt.isPresent()) {
            redirectAttributes.addFlashAttribute("error", "Proveedor no encontrado.");
            return "redirect:/proveedor";
        }

        List<Empresa> empresas = empresaService.findAll();

        model.addAttribute("proveedor", proveedorOpt.get());
        model.addAttribute("empresas", empresas);
        model.addAttribute("active_page", "proveedor");
        return "proveedor/editar";
    }

    @PostMapping("/actualizar/{id}")
    public String actualizarProveedor(
            @PathVariable Integer id,
            @RequestParam String ruc,
            @RequestParam String razonSocial,
            @RequestParam(required = false) String nombreComercial,
            @RequestParam(required = false) String direccion,
            @RequestParam(required = false) String telefono,
            @RequestParam(required = false) String correo,
            @RequestParam Integer estadoEmpresa,
            @RequestParam Integer idEstado,
            RedirectAttributes redirectAttributes) {
    
        Optional<Proveedor> proveedorOpt = proveedorService.findById(id);
    
        if (!proveedorOpt.isPresent()) {
            redirectAttributes.addFlashAttribute("error", "Proveedor no encontrado.");
            return "redirect:/proveedor";
        }
    
        Proveedor proveedor = proveedorOpt.get();
        Empresa empresa = proveedor.getEmpresa();
    
        // Validación: RUC debe tener 11 dígitos
        if (!ruc.matches("\\d{11}")) {
            redirectAttributes.addFlashAttribute("error", "El RUC debe tener exactamente 11 dígitos.");
            return "redirect:/proveedor/editar/" + id;
        }
    
        // Validación: evitar duplicado de RUC en otra empresa
        Optional<Empresa> empresaByRuc = empresaService.buscarPorRuc(ruc);
        if (empresaByRuc.isPresent() && !empresaByRuc.get().getIdEmpresa().equals(empresa.getIdEmpresa())) {
            redirectAttributes.addFlashAttribute("error", "Ya existe otra empresa con ese RUC.");
            return "redirect:/proveedor/editar/" + id;
        }
    
        // Validación: evitar duplicado de razón social en otra empresa
        Optional<Empresa> empresaByRazon = empresaService.buscarPorRazonSocial(razonSocial);
        if (empresaByRazon.isPresent() && !empresaByRazon.get().getIdEmpresa().equals(empresa.getIdEmpresa())) {
            redirectAttributes.addFlashAttribute("error", "Ya existe otra empresa con esa Razón Social.");
            return "redirect:/proveedor/editar/" + id;
        }
    
        try {
            // Actualizar empresa
            empresa.setRuc(ruc);
            empresa.setRazonSocial(razonSocial);
            empresa.setNombreComercial(nombreComercial);
            empresa.setDireccion(direccion);
            empresa.setTelefono(telefono);
            empresa.setCorreo(correo);
            empresa.setIdEstado(estadoEmpresa);
            empresaService.guardar(empresa);
    
            // Actualizar proveedor
            proveedor.setIdEstado(idEstado);
            proveedorService.save(proveedor);
    
            redirectAttributes.addFlashAttribute("success", "Proveedor actualizado correctamente.");
            return "redirect:/proveedor";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al actualizar: " + e.getMessage());
            return "redirect:/proveedor/editar/" + id;
        }
    }
    

    @PostMapping("/eliminar/{id}")
    public String eliminarProveedor(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            Optional<Proveedor> proveedorOpt = proveedorService.findById(id);
            if (!proveedorOpt.isPresent()) {
                redirectAttributes.addFlashAttribute("error", "Proveedor no encontrado.");
                return "redirect:/proveedor";
            }

            Proveedor proveedor = proveedorOpt.get();
            proveedor.setIdEstado(0); // Eliminación lógica
            proveedorService.save(proveedor);

            redirectAttributes.addFlashAttribute("success", "Proveedor eliminado correctamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar el proveedor: " + e.getMessage());
        }
        return "redirect:/proveedor";
    }
}
