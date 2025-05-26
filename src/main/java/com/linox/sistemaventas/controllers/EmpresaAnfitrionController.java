package com.linox.sistemaventas.controllers;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.linox.sistemaventas.models.EmpresaAnfitrion;
import com.linox.sistemaventas.services.EmpresaAnfitrionService;

@Controller
@RequestMapping("/empresa-anfitrion")
public class EmpresaAnfitrionController {

    @Autowired
    private EmpresaAnfitrionService empresaAnfitrionService;

    // Mostrar formulario de edición
    @GetMapping("/editar/{id}")
    public String editarEmpresa(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        Optional<EmpresaAnfitrion> empresaOpt = empresaAnfitrionService.findById(id);
        if (!empresaOpt.isPresent()) {
            redirectAttributes.addFlashAttribute("error", "Empresa no encontrada.");
            return "redirect:/dashboard";
        }

        model.addAttribute("empresa", empresaOpt.get());
        model.addAttribute("active_page", "empresa-anfitrion");
        return "empresa_anfitrion/editar";
    }

    // Actualizar empresa
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
            @RequestParam(value = "logo", required = false) MultipartFile logo,
            RedirectAttributes redirectAttributes) {

        Optional<EmpresaAnfitrion> empresaOpt = empresaAnfitrionService.findById(id);

        if (!empresaOpt.isPresent()) {
            redirectAttributes.addFlashAttribute("error", "Empresa no encontrada.");
            return "redirect:/empresa";
        }

        try {
            EmpresaAnfitrion empresa = empresaOpt.get();
            empresa.setRuc(ruc);
            empresa.setRazonSocial(razonSocial);
            empresa.setNombreComercial(nombreComercial);
            empresa.setDireccion(direccion);
            empresa.setTelefono(telefono);
            empresa.setCorreo(correo);
            empresa.setIdEstado(idEstado);

            if (logo != null && !logo.isEmpty()) {
                String uploadDir = "uploads/empresa/";
                String fileName = UUID.randomUUID().toString() + "_" + logo.getOriginalFilename();
                Path uploadPath = Paths.get(uploadDir);

                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                Path filePath = uploadPath.resolve(fileName);
                Files.copy(logo.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

                empresa.setLogoUrl("/uploads/empresa/" + fileName);
            }

            empresaAnfitrionService.save(empresa);
            redirectAttributes.addFlashAttribute("success", "Empresa actualizada correctamente.");

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al actualizar la empresa: " + e.getMessage());
        }

        return "redirect:/empresa";
    }

    // Suponiendo que tu clase es EmpresaAnfitrionController
    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<Map<String, String>> getEmpresaAnfitrion(@PathVariable Integer id) {
        Optional<EmpresaAnfitrion> empresaOpt = empresaAnfitrionService.findById(id);
        if (empresaOpt.isPresent()) {
            Map<String, String> data = new HashMap<>();
            data.put("logo", empresaOpt.get().getLogoUrl());
            return ResponseEntity.ok(data);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
