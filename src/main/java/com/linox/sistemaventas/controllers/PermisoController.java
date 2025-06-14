package com.linox.sistemaventas.controllers;

import com.linox.sistemaventas.entities.Permiso;
import com.linox.sistemaventas.services.PermisoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/permisos")
public class PermisoController {

    @Autowired
    private PermisoService permisoService;

    @GetMapping
    public List<Permiso> getAll() {
        return permisoService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Permiso> getById(@PathVariable Integer id) {
        return permisoService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Permiso> create(@RequestBody Permiso permiso) {
        return ResponseEntity.ok(permisoService.save(permiso));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Permiso> update(@PathVariable Integer id, @RequestBody Permiso permiso) {
        return permisoService.findById(id)
                .map(p -> {
                    permiso.setIdPermiso(id); // asegurar que se actualice el correcto
                    return ResponseEntity.ok(permisoService.save(permiso));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        if (permisoService.findById(id).isPresent()) {
            permisoService.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
