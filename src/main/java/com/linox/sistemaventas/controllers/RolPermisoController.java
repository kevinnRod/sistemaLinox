package com.linox.sistemaventas.controllers;

import com.linox.sistemaventas.models.RolPermiso;
import com.linox.sistemaventas.models.RolPermisoId;
import com.linox.sistemaventas.services.RolPermisoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rol-permisos")
public class RolPermisoController {

    @Autowired
    private RolPermisoService rolPermisoService;

    @GetMapping
    public List<RolPermiso> getAll() {
        return rolPermisoService.findAll();
    }

    @GetMapping("/{idRol}/{idPermiso}")
    public ResponseEntity<RolPermiso> getById(@PathVariable Integer idRol, @PathVariable Integer idPermiso) {
        RolPermisoId id = new RolPermisoId(idRol, idPermiso);
        return rolPermisoService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/rol/{idRol}")
    public List<RolPermiso> getByRolId(@PathVariable Integer idRol) {
        return rolPermisoService.findByRolId(idRol);
    }

    @PostMapping
    public ResponseEntity<RolPermiso> create(@RequestBody RolPermiso rolPermiso) {
        return ResponseEntity.ok(rolPermisoService.save(rolPermiso));
    }

    @DeleteMapping("/{idRol}/{idPermiso}")
    public ResponseEntity<Void> delete(@PathVariable Integer idRol, @PathVariable Integer idPermiso) {
        RolPermisoId id = new RolPermisoId(idRol, idPermiso);
        rolPermisoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}