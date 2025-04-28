package com.linox.sistemaventas.controllers;

import com.linox.sistemaventas.models.Usuario;
import com.linox.sistemaventas.repositories.UsuarioRepository;
import com.linox.sistemaventas.services.UsuarioService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/usuario") // Ruta base para las vistas de usuario
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

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
        Usuario usuario = new Usuario();
        model.addAttribute("usuario", usuario); // Pasamos un usuario vacío para el formulario
        return "usuario/crearUsuario"; // Vuelve a la vista usuario/crear.html
    }

    // Crear un nuevo usuario
    @PostMapping("/usuario/save")
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

        usuarioRepository.save(user);
        System.out.println(usuario);
        return "redirect:/usuario";
    }

    // Actualizar un usuario por su ID
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id, @RequestBody Usuario usuario) {
        Optional<Usuario> existente = usuarioService.findById(id);

        if (!existente.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Usuario usuarioExistente = existente.get();

        // Solo actualizamos campos permitidos
        usuarioExistente.setUsuario(usuario.getUsuario());
        usuarioExistente.setCorreo(usuario.getCorreo());
        usuarioExistente.setContrasenaEnc(usuario.getContrasenaEnc());
        usuarioExistente.setUrlFoto(usuario.getUrlFoto());
        usuarioExistente.setIdEstado(usuario.getIdEstado());

        Usuario actualizado = usuarioService.save(usuarioExistente);
        return ResponseEntity.ok(actualizado);
    }

    // Cambiar el estado a inactivo
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        Optional<Usuario> existente = usuarioService.findById(id);
        if (existente.isPresent()) {
            Usuario usuario = existente.get();
            usuario.setIdEstado(0); // Cambiar el estado a inactivo (0)
            usuarioService.save(usuario); // Guardar el cambio
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
