package com.linox.sistemaventas.controllers;

import com.linox.sistemaventas.entities.Usuario;
import com.linox.sistemaventas.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public List<Usuario> getAll() {
        return usuarioService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> getById(@PathVariable Integer id) {
        Optional<Usuario> usuario = usuarioService.findById(id);
        return usuario.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Usuario usuario) {
        if (usuario.getUsuario() == null || usuario.getContrasenaEnc() == null) {
            return ResponseEntity.badRequest().body("Usuario y contraseña son requeridos.");
        }

        if (usuarioService.findByUsuario(usuario.getUsuario()).isPresent()) {
            return ResponseEntity.badRequest().body("El nombre de usuario ya existe.");
        }

        usuario.setIdUsuario(null); // Asegura que no venga con un ID
        Usuario creado = usuarioService.save(usuario);
        return ResponseEntity.ok(creado);
    }

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

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        Optional<Usuario> existente = usuarioService.findById(id);
        if (existente.isPresent()) {
            usuarioService.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
