package com.linox.sistemaventas.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.linox.sistemaventas.models.Producto;
import com.linox.sistemaventas.models.Sucursal;
import com.linox.sistemaventas.services.InventarioService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/dashboard/inventario")
public class InventarioController {

    @Autowired
    private InventarioService inventarioService;

    @GetMapping
    public String mostrarDashboardInventario(Model model, HttpSession session) {
        List<String> roles = (List<String>) session.getAttribute("roles");
        if (roles == null || !roles.contains("ADMIN")) {
            return "redirect:/acceso-denegado";
        }

        // Obtener todos los productos activos
        List<Producto> productosActivos = inventarioService.obtenerTodosLosProductos();
        int totalProductos = productosActivos.size();

        // Obtener productos con bajo stock
        List<Producto> bajoStock = inventarioService.obtenerProductosConBajoStock();
        int productosBajoStock = bajoStock.size();

        List<String> nombres = bajoStock.stream()
                .map(Producto::getNombreProducto)
                .toList();
        List<Integer> stocks = bajoStock.stream()
                .map(Producto::getStock)
                .toList();

        // Agrupar productos por sucursal
        Map<Sucursal, Long> productosPorSucursal = productosActivos.stream()
                .filter(p -> p.getSucursal() != null)
                .collect(Collectors.groupingBy(
                        Producto::getSucursal,
                        Collectors.counting()));

        List<String> sucursalNombres = productosPorSucursal.keySet().stream()
                .map(Sucursal::getNombreSucursal)
                .toList();
        List<Long> sucursalCantidades = new ArrayList<>(productosPorSucursal.values());

        // Agrupar productos con bajo stock por sucursal para el filtro dinámico
        Map<String, List<Map<String, Object>>> bajoStockPorSucursal = new HashMap<>();
        Map<String, Map<String, Object>> productosUnicos = new HashMap<>(); // Para "TODAS"

        for (Producto p : bajoStock) {
            if (p.getSucursal() != null) {
                String sucursalNombre = p.getSucursal().getNombreSucursal();

                bajoStockPorSucursal.putIfAbsent(sucursalNombre, new ArrayList<>());

                Map<String, Object> productoMap = new HashMap<>();
                productoMap.put("codProducto", p.getCodProducto());
                productoMap.put("nombreProducto", p.getNombreProducto());
                productoMap.put("stock", p.getStock());
                productoMap.put("sucursal", Map.of("nombreSucursal", sucursalNombre));

                bajoStockPorSucursal.get(sucursalNombre).add(productoMap);

                // Añadir a productos únicos para "TODAS"
                productosUnicos.putIfAbsent(p.getCodProducto(), productoMap);
            }
        }

        // Agregar la lista única de productos a "TODAS"
        bajoStockPorSucursal.put("TODAS", new ArrayList<>(productosUnicos.values()));

        // Agregar datos al modelo
        model.addAttribute("active_page", "inventario");
        model.addAttribute("totalProductos", totalProductos);
        model.addAttribute("productosBajoStock", productosBajoStock);
        model.addAttribute("bajoStock", bajoStock); // para la tabla
        model.addAttribute("nombres", nombres); // gráfico bajo stock inicial
        model.addAttribute("stocks", stocks); // gráfico bajo stock inicial
        model.addAttribute("sucursalNombres", sucursalNombres); // para gráfico por sucursal y select
        model.addAttribute("sucursalCantidades", sucursalCantidades);
        model.addAttribute("sucursales", productosPorSucursal.keySet().stream()
                .collect(Collectors.toMap(
                        Sucursal::getNombreSucursal,
                        Sucursal::getNombreSucursal
                ))); // total sucursales
        model.addAttribute("bajoStockPorSucursal", bajoStockPorSucursal); // para JS dinámico

        return "dashboard/inventario";
    }
}
