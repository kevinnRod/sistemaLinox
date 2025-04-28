package com.linox.sistemaventas.controllers;

import com.linox.sistemaventas.models.UsuarioRol;
import com.linox.sistemaventas.models.UsuarioRolId;
import com.linox.sistemaventas.services.UsuarioRolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuario-roles")
public class UsuarioRolController {

    @Autowired
    private UsuarioRolService usuarioRolService;

    @GetMapping
    public List<UsuarioRol> getAll() {
        return usuarioRolService.findAll();
    }

    @GetMapping("/{idUsuario}/{idRol}")
    public ResponseEntity<UsuarioRol> getById(@PathVariable Integer idUsuario, @PathVariable Integer idRol) {
        UsuarioRolId id = new UsuarioRolId(idUsuario, idRol);
        return usuarioRolService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/usuario/{idUsuario}")
    public List<UsuarioRol> getByUsuarioId(@PathVariable Integer idUsuario) {
        return usuarioRolService.findByUsuarioId(idUsuario);
    }

    @PostMapping
    public ResponseEntity<UsuarioRol> create(@RequestBody UsuarioRol usuarioRol) {
        return ResponseEntity.ok(usuarioRolService.save(usuarioRol));
    }

    @DeleteMapping("/{idUsuario}/{idRol}")
    public ResponseEntity<Void> delete(@PathVariable Integer idUsuario, @PathVariable Integer idRol) {
        UsuarioRolId id = new UsuarioRolId(idUsuario, idRol);
        usuarioRolService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
