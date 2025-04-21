package com.linox.sistemaventas.controllers;

import com.linox.sistemaventas.entities.Usuario;
import com.linox.sistemaventas.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/usuarios")
public class UsuarioViewController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public String listarUsuarios(Model model) {
        model.addAttribute("active_page", "usuario");
        model.addAttribute("usuarios", usuarioService.findAll());
        return "usuario/usuarios"; // Vista: usuarios.html
    }

    @GetMapping("/crear")
    public String mostrarFormularioCrear(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "crear"; // Vista: crear.html
    }

    @PostMapping("/guardar")
    public String guardarUsuario(@ModelAttribute Usuario usuario) {
        usuarioService.save(usuario);
        return "redirect:/usuarios";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Integer id, Model model) {
        Usuario usuario = usuarioService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ID de usuario inválido: " + id));
        model.addAttribute("usuario", usuario);
        return "editar"; // Vista: editar.html
    }

    @PostMapping("/actualizar/{id}")
    public String actualizarUsuario(@PathVariable Integer id, @ModelAttribute Usuario usuario) {
        Usuario existente = usuarioService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ID de usuario inválido: " + id));

        existente.setUsuario(usuario.getUsuario());
        existente.setCorreo(usuario.getCorreo());
        existente.setContrasenaEnc(usuario.getContrasenaEnc());
        existente.setIdEstado(usuario.getIdEstado());
        existente.setUrlFoto(usuario.getUrlFoto());

        usuarioService.save(existente);
        return "redirect:/usuarios";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarUsuario(@PathVariable Integer id) {
        usuarioService.deleteById(id);
        return "redirect:/usuarios";
    }
}
