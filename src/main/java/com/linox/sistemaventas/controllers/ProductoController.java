package com.linox.sistemaventas.controllers;

import com.linox.sistemaventas.models.Producto;
import com.linox.sistemaventas.services.ProductoService;
import com.linox.sistemaventas.services.SucursalService;
import com.linox.sistemaventas.services.UnidadMedidaService;

import groovyjarjarantlr4.v4.parse.ANTLRParser.qid_return;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

import java.util.Optional;

@Controller
@RequestMapping("/productos")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @Autowired
    private UnidadMedidaService unidadMedidaService;

    @Autowired
    private SucursalService sucursalService;

    // Mostrar lista
    @GetMapping
    public String listarProductos(Model model) {
        model.addAttribute("productos", productoService.findAllActivos());
        model.addAttribute("active_page", "producto");
        return "productos/lista";
    }

    // Mostrar formulario de creación
    @GetMapping("/create")
    public String mostrarFormularioCrear(Model model) {
        model.addAttribute("producto", new Producto());
        model.addAttribute("unidades", unidadMedidaService.findAllActivos());
        model.addAttribute("active_page", "producto");
        model.addAttribute("sucursales", sucursalService.findAllActivos());

        return "productos/crear";
    }

    @PostMapping("/save")
    public String guardarProducto(@ModelAttribute Producto producto, Model model) {
        List<Producto> existentes = productoService.findByCodProductoAndIdEstado(producto.getCodProducto(), 1);
    
        if (!existentes.isEmpty()) {
            model.addAttribute("error", "Ya existe un producto activo con ese código.");
            model.addAttribute("producto", producto);
            model.addAttribute("unidades", unidadMedidaService.findAllActivos());
            model.addAttribute("sucursales", sucursalService.findAllActivos());

            model.addAttribute("active_page", "producto");

            return "productos/crear";
        }
    
        producto.setIdEstado(1); // Activo
        productoService.save(producto);
        return "redirect:/productos";
    }
    
    

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Integer id, Model model) {
        Optional<Producto> productoOpt = productoService.findById(id);
        if (productoOpt.isPresent()) {
            model.addAttribute("producto", productoOpt.get());
            model.addAttribute("unidades", unidadMedidaService.findAllActivos());
            model.addAttribute("sucursales", sucursalService.findAllActivos()); // ✅ Agregado
            model.addAttribute("active_page", "producto");
            return "productos/editar";
        } else {
            return "redirect:/productos";
        }
    }
    

    @PostMapping("/update")
    public String actualizarProducto(@ModelAttribute Producto producto, Model model) {
        List<Producto> existentes = productoService.findByCodProductoAndIdEstado(producto.getCodProducto(), 1);
    
        // Validar duplicado en otros productos activos
        boolean existeOtro = existentes.stream()
            .anyMatch(p -> !p.getId().equals(producto.getId()));
    
            if (existeOtro) {
                model.addAttribute("error", "Ya existe otro producto activo con ese código.");
                model.addAttribute("producto", producto);
                model.addAttribute("unidades", unidadMedidaService.findAllActivos());
                model.addAttribute("sucursales", sucursalService.findAllActivos()); // ✅ Faltaba aquí
                model.addAttribute("active_page", "producto");
                return "productos/editar";
            }
            
    
        // Restaurar createdAt
        Optional<Producto> originalOpt = productoService.findById(producto.getId());
        if (originalOpt.isPresent()) {
            producto.setCreatedAt(originalOpt.get().getCreatedAt());
            productoService.save(producto);
        }
    
        return "redirect:/productos";
    }
    
    
    

    // Eliminar (borrado lógico)
    @PostMapping("/eliminar/{id}")
    public String eliminarLogicamente(@PathVariable Integer id) {
        Optional<Producto> productoOpt = productoService.findById(id);
        if (productoOpt.isPresent()) {
            Producto producto = productoOpt.get();
            producto.setIdEstado(2); // Inactivo
            productoService.save(producto);
        }
        return "redirect:/productos";
    }
}
