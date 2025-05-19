package com.linox.sistemaventas.controllers;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.linox.sistemaventas.models.Permiso;
import com.linox.sistemaventas.models.Rol;
import com.linox.sistemaventas.models.RolPermiso;
import com.linox.sistemaventas.services.PermisoService;
import com.linox.sistemaventas.services.RolPermisoService;
import com.linox.sistemaventas.services.RolService;

@Controller
@RequestMapping("/rolPermiso")
public class RolPermisoController {

    @Autowired
    private RolService rolService;

    @Autowired
    private PermisoService permisoService;

    @Autowired
    private RolPermisoService rolPermisoService;

    @PostMapping("/asignar")
    public String asignarPermiso(
            @RequestParam("idRol") Integer idRol,
            @RequestParam("idPermiso") Integer idPermiso,
            RedirectAttributes redirectAttributes) {

        if (idRol == null || idPermiso == null || idRol <= 0 || idPermiso <= 0) {
            redirectAttributes.addFlashAttribute("error", "Parámetros inválidos.");
            return "redirect:/rol/editar/" + idRol;
        }

        Optional<Rol> rolOpt = rolService.findById(idRol);
        Optional<Permiso> permisoOpt = permisoService.findById(idPermiso);

        if (!rolOpt.isPresent() || !permisoOpt.isPresent()) {
            redirectAttributes.addFlashAttribute("error", "Rol o permiso no encontrado.");
            return "redirect:/rol/editar/" + idRol;
        }

        Rol rol = rolOpt.get();
        Permiso permiso = permisoOpt.get();

        boolean yaAsignado = rolPermisoService.existsByRolPermiso(rol, permiso);

        if (yaAsignado) {
            Optional<RolPermiso> rolPermisoOpt = rolPermisoService.findByRolAndPermiso(rol, permiso);

            if (rolPermisoOpt.get().getIdEstado() == 0) {
                RolPermiso rolPermiso = rolPermisoOpt.get();
                rolPermiso.setIdEstado(1); // Restaurar relación
                rolPermisoService.save(rolPermiso);
                redirectAttributes.addFlashAttribute("success", "Permiso asignado correctamente.");
            } else {
                redirectAttributes.addFlashAttribute("error", "El permiso ya está asignado al rol.");
            }

        } else {
            RolPermiso rolPermiso = new RolPermiso(rol, permiso);
            rolPermiso.setIdEstado(1);
            rolPermisoService.save(rolPermiso);
            redirectAttributes.addFlashAttribute("success", "Permiso asignado correctamente.");
        }

        return "redirect:/rol/editar/" + idRol;
    }

    @PostMapping("/eliminar")
    public String eliminarPermisoAsignado(
            @RequestParam("idRol") Integer idRol,
            @RequestParam("idPermiso") Integer idPermiso,
            RedirectAttributes redirectAttributes) {

        if (idRol == null || idPermiso == null || idRol <= 0 || idPermiso <= 0) {
            redirectAttributes.addFlashAttribute("error", "Parámetros inválidos.");
            return "redirect:/rol/editar/" + idRol;
        }

        Optional<Rol> rolOpt = rolService.findById(idRol);
        Optional<Permiso> permisoOpt = permisoService.findById(idPermiso);

        if (!rolOpt.isPresent() || !permisoOpt.isPresent()) {
            redirectAttributes.addFlashAttribute("error", "Rol o permiso no encontrado.");
            return "redirect:/rol/editar/" + idRol;
        }

        Rol rol = rolOpt.get();
        Permiso permiso = permisoOpt.get();

        Optional<RolPermiso> rolPermisoOpt = rolPermisoService.findByRolAndPermiso(rol, permiso);

        if (rolPermisoOpt.isPresent()) {
            RolPermiso rolPermiso = rolPermisoOpt.get();
            rolPermiso.setIdEstado(0); // Eliminación lógica
            rolPermisoService.save(rolPermiso);
            redirectAttributes.addFlashAttribute("success", "Permiso eliminado correctamente.");
        } else {
            redirectAttributes.addFlashAttribute("error", "El permiso no está asignado al rol.");
        }

        return "redirect:/rol/editar/" + idRol;
    }

    @GetMapping("/permisos/{idRol}")
    @ResponseBody
    public ResponseEntity<List<String>> obtenerPermisosPorRol(@PathVariable("idRol") Integer idRol) {
        List<RolPermiso> permisosAsignados = rolPermisoService.findAllByEstadoActivo(idRol);

        // Devuelve una lista vacía con estado 200 OK si no hay permisos
        List<String> nombresPermisos = permisosAsignados.stream()
                .map(rp -> rp.getPermiso().getNombrePermiso())
                .collect(Collectors.toList());

        return ResponseEntity.ok(nombresPermisos);
    }

}
