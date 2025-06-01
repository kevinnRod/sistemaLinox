package com.linox.sistemaventas.controllers;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.linox.sistemaventas.models.Usuario;
import com.linox.sistemaventas.models.UsuarioRol;
import com.linox.sistemaventas.repositories.UsuarioRepository;
import com.linox.sistemaventas.services.UsuarioRolService;

import jakarta.servlet.http.HttpSession;

@Controller
public class DashboardController {

    @Autowired
    private UsuarioRepository usuarioRepo;

    @Autowired
    private UsuarioRolService usuarioRolService;

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        Optional<Usuario> usuarioOpt = usuarioRepo.findByUsuario(username);
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();

            // Obtener solo roles activos
            List<UsuarioRol> rolesActivos = usuarioRolService.findAllByEstadoActivo(usuario.getIdUsuario());
            List<String> roles = rolesActivos.stream()
                    .map(rol -> rol.getRol().getNombreRol())
                    .collect(Collectors.toList());

            // Guardar en sesión
            session.setAttribute("roles", roles);

            // Para depurar:
            System.out.println("Roles en sesión: " + session.getAttribute("roles"));
        }

        return "redirect:/inicio";
    }

    @GetMapping("/inicio")
    public String inicio(Model model) {

        model.addAttribute("active_page", "inicio");
        return "hola";
    }

}
