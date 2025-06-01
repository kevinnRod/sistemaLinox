package com.linox.sistemaventas.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.linox.sistemaventas.models.Rol;
import com.linox.sistemaventas.models.Usuario;
import com.linox.sistemaventas.models.UsuarioRol;
import com.linox.sistemaventas.services.RolService;
import com.linox.sistemaventas.services.UsuarioRolService;
import com.linox.sistemaventas.services.UsuarioService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/usuarioRol")
public class UsuarioRolController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private RolService rolService;

    @Autowired
    private UsuarioRolService usuarioRolService;

    @PostMapping("/asignar")
    public String asignarRol(
            @RequestParam("idUsuario") Integer idUsuario,
            @RequestParam("idRol") Integer idRol,
            HttpSession session, // <- inyecta la sesión aquí
            RedirectAttributes redirectAttributes) {

        // Obtenemos roles de la sesión
        @SuppressWarnings("unchecked")
        List<String> roles = (List<String>) session.getAttribute("roles");

        // Validamos que el usuario tenga rol ADMIN
        if (roles == null || !roles.contains("ADMIN")) {
            redirectAttributes.addFlashAttribute("error", "No tienes permiso para realizar esta acción.");
            return "redirect:/usuario/editar/" + idUsuario; // o la ruta que uses para acceso denegado
        }

        if (idUsuario == null || idRol == null || idUsuario <= 0 || idRol <= 0) {
            redirectAttributes.addFlashAttribute("error", "Parámetros inválidos.");
            return "redirect:/usuario/ver/" + idUsuario; // Redirigir a una lista de usuarios si los parámetros son
                                                         // incorrectos
        }

        Optional<Usuario> usuarioOpt = usuarioService.findById(idUsuario);
        Optional<Rol> rolOpt = rolService.findById(idRol);

        if (!usuarioOpt.isPresent() || !rolOpt.isPresent()) {
            redirectAttributes.addFlashAttribute("error", "Usuario o rol no encontrado.");
            return "redirect:/usuario/ver/" + idUsuario; // Redirigir a la lista si no se encuentran el usuario o rol
        }

        Usuario usuario = usuarioOpt.get();
        Rol rol = rolOpt.get();

        boolean yaAsignado = usuarioRolService.existsByUsuarioAndRol(usuario, rol);

        if (yaAsignado) {

            Optional<UsuarioRol> usuarioRolOpt = usuarioRolService.findByUsuarioAndRol(usuario, rol);

            if (usuarioRolOpt.get().getIdEstado() == 0) {
                UsuarioRol usuarioRol = usuarioRolOpt.get();
                usuarioRol.setIdEstado(1); // Eliminación lógica
                usuarioRolService.save(usuarioRol); // Guardar cambios
                redirectAttributes.addFlashAttribute("success", "Rol Guardado correctamente.");
            } else {
                redirectAttributes.addFlashAttribute("error", "El rol ya está asignado al usuario.");
            }

        } else {
            UsuarioRol usuarioRol = new UsuarioRol(usuario, rol);
            usuarioRol.setIdEstado(1);
            usuarioRolService.save(usuarioRol);
            redirectAttributes.addFlashAttribute("success", "Rol asignado correctamente.");
        }

        return "redirect:/usuario/editar/" + idUsuario;
    }

    @PostMapping("/eliminar")
    public String eliminarRolAsignado(
            @RequestParam("idUsuario") Integer idUsuario,
            @RequestParam("idRol") Integer idRol,
            HttpSession session, // <- inyecta la sesión aquí
            RedirectAttributes redirectAttributes) {

        // Obtenemos roles de la sesión
        @SuppressWarnings("unchecked")
        List<String> roles = (List<String>) session.getAttribute("roles");

        // Validamos que el usuario tenga rol ADMIN
        if (roles == null || !roles.contains("ADMIN")) {
            redirectAttributes.addFlashAttribute("error", "No tienes permiso para realizar esta acción.");
            return "redirect:/usuario/editar/" + idUsuario; // o la ruta que uses para acceso denegado
        }

        if (idUsuario == null || idRol == null || idUsuario <= 0 || idRol <= 0) {
            redirectAttributes.addFlashAttribute("error", "Parámetros inválidos.");
            return "redirect:/usuario/editar/" + idUsuario;
        }

        Optional<Usuario> usuarioOpt = usuarioService.findById(idUsuario);
        Optional<Rol> rolOpt = rolService.findById(idRol);

        if (!usuarioOpt.isPresent() || !rolOpt.isPresent()) {
            redirectAttributes.addFlashAttribute("error", "Usuario o rol no encontrado.");
            return "redirect:/usuario/editar/" + idUsuario;
        }

        Usuario usuario = usuarioOpt.get();
        Rol rol = rolOpt.get();

        Optional<UsuarioRol> usuarioRolOpt = usuarioRolService.findByUsuarioAndRol(usuario, rol);

        if (usuarioRolOpt.isPresent()) {
            UsuarioRol usuarioRol = usuarioRolOpt.get();
            usuarioRol.setIdEstado(0); // Eliminación lógica
            usuarioRolService.save(usuarioRol); // Guardar cambios
            redirectAttributes.addFlashAttribute("success", "Rol eliminado correctamente.");
        } else {
            redirectAttributes.addFlashAttribute("error", "El rol no está asignado al usuario.");
        }

        return "redirect:/usuario/editar/" + idUsuario;
    }

}
