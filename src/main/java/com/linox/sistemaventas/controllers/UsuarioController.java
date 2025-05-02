package com.linox.sistemaventas.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.linox.sistemaventas.models.Usuario;
import com.linox.sistemaventas.services.UsuarioService;

@Controller
@RequestMapping("/usuario") // Ruta base para las vistas de usuario
public class UsuarioController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UsuarioService usuarioService;

    // Obtener todos los usuarios y redirigir a la vista
    @GetMapping
    public String getAll(Model model) {
        List<Usuario> usuarios = usuarioService.findAllByEstadoActivo();
        model.addAttribute("usuarios", usuarios);// Pasar la lista de usuarios a la vista
        model.addAttribute("active_page", "usuario");
        return "usuario/usuarios"; // Nombre de la vista Thymeleaf
    }

    // Mostrar el formulario de creación de un nuevo usuario
    @GetMapping("/create")
    public String crearUsuario(Model model) {
        model.addAttribute("active_page", "usuario");
        return "usuario/crearUsuario"; // Vuelve a la vista usuario/crear.html
    }

    // Crear un nuevo usuario
    @PostMapping("/save")
    public String saveUsuario(
            @RequestParam("usuario") String usuario,
            @RequestParam("correo") String correo,
            @RequestParam("contrasenaEnc") String contrasenaEnc,
            @RequestParam("idEstado") Integer idEstado) {
        Usuario user = new Usuario();
        user.setUsuario(usuario);
        user.setCorreo(correo);
        user.setContrasenaEnc(passwordEncoder.encode(contrasenaEnc));
        user.setIdEstado(idEstado);
        usuarioService.save(user);
        return "redirect:/usuario";
    }

    @GetMapping("/editar/{id}")
    public String editarUsuario(@PathVariable Integer id, Model model) {
        Optional<Usuario> existente = usuarioService.findById(id);
        if (!existente.isPresent()) {
            return "redirect:/usuario";
        }
        Usuario usuarioExistente = existente.get();
        model.addAttribute("usuario", usuarioExistente);
        model.addAttribute("active_page", "usuario");
        return "usuario/editarUsuario"; // Vuelve a la vista usuario/crear.html
    }

    @PostMapping("/actualizar/{id}")
    public String actualizarUsuario(@PathVariable Integer id, RedirectAttributes redirectAttributes,
            @RequestParam("usuario") String usuario,
            @RequestParam("correo") String correo,
            @RequestParam("contrasena") String contrasena,
            @RequestParam("idEstado") Integer idEstado) {
        Optional<Usuario> usuarioOpt = usuarioService.findById(id);

        if (!usuarioOpt.isPresent()) {
            redirectAttributes.addFlashAttribute("error", "El usuario no fue encontrado.");
            return "redirect:/usuario";
        }
        Usuario user = usuarioOpt.get();
        user.setUsuario(usuario);
        user.setCorreo(correo);
        if (contrasena != "") {
            user.setContrasenaEnc(passwordEncoder.encode(contrasena));
        }
        user.setIdEstado(idEstado);
        usuarioService.save(user);
        return "redirect:/usuario";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminarUsuario(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        Optional<Usuario> usuarioOpt = usuarioService.findById(id);

        if (!usuarioOpt.isPresent()) {
            redirectAttributes.addFlashAttribute("error", "El usuario no fue encontrado.");
            return "redirect:/usuario";
        }

        Usuario usuario = usuarioOpt.get();
        usuario.setIdEstado(0); // Cambia el estado a inactivo
        usuarioService.save(usuario);

        redirectAttributes.addFlashAttribute("success", "Usuario inactivado correctamente.");
        return "redirect:/usuario";
    }

}
