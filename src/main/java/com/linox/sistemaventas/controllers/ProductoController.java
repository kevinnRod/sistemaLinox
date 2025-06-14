package com.linox.sistemaventas.controllers;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.linox.sistemaventas.models.CategoriaProducto;
import com.linox.sistemaventas.models.Producto;
import com.linox.sistemaventas.services.CategoriaProductoService;
import com.linox.sistemaventas.services.ProductoService;
import com.linox.sistemaventas.services.ProveedorService;
import com.linox.sistemaventas.services.SucursalService;
import com.linox.sistemaventas.services.UnidadMedidaService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/productos")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @Autowired
    private UnidadMedidaService unidadMedidaService;

    @Autowired
    private SucursalService sucursalService;

    @Autowired
    private CategoriaProductoService categoriaProductoService;

    @Autowired
    private ProveedorService proveedorService;

    // Mostrar lista
    @GetMapping
    public String listarProductos(Model model) {
        model.addAttribute("productos", productoService.findAllActivos());
        model.addAttribute("active_page", "producto");
        return "productos/lista";
    }

    // Mostrar formulario de creación
    @GetMapping("/create")
    public String mostrarFormularioCrear(Model model, HttpServletRequest request) {
        Producto producto = new Producto();
        producto.setCodProducto(generarCodigoProducto());
        // Leer la cookie
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("categoria_id".equals(cookie.getName())) {
                    try {
                        Integer categoriaId = Integer.parseInt(cookie.getValue());

                        Optional<CategoriaProducto> categoria1 = categoriaProductoService.findById(categoriaId);
                        producto.setCategoria(categoria1.get());
                    } catch (NumberFormatException e) {
                        // ignora si la cookie no es válida
                    }
                }
            }
        }
        model.addAttribute("producto", producto);
        model.addAttribute("unidades", unidadMedidaService.findAllActivos());
        model.addAttribute("active_page", "producto");
        model.addAttribute("sucursales", sucursalService.findAllActivos());
        model.addAttribute("categorias", categoriaProductoService.findAllActivos());
        model.addAttribute("proveedores", proveedorService.findAllActivos());

        return "productos/crear";
    }

    @PostMapping("/save")
    public String guardarProducto(@ModelAttribute Producto producto, Model model, HttpServletResponse response) {
        List<Producto> existentes = productoService.findByCodProductoAndIdEstado(producto.getCodProducto(), 1);

        if (!existentes.isEmpty()) {
            model.addAttribute("error", "Ya existe un producto activo con ese código.");
            model.addAttribute("producto", producto);
            model.addAttribute("unidades", unidadMedidaService.findAllActivos());
            model.addAttribute("active_page", "producto");
            model.addAttribute("sucursales", sucursalService.findAllActivos());
            model.addAttribute("categorias", categoriaProductoService.findAllActivos());
            model.addAttribute("proveedores", proveedorService.findAllActivos());

            return "productos/crear";
        }

        producto.setIdEstado(1); // Activo
        if (producto.getStock() < 0) {
            model.addAttribute("error", "stock no puede ser menor a cero.");
            model.addAttribute("producto", producto);
            model.addAttribute("unidades", unidadMedidaService.findAllActivos());
            model.addAttribute("active_page", "producto");
            model.addAttribute("sucursales", sucursalService.findAllActivos());
            model.addAttribute("categorias", categoriaProductoService.findAllActivos());
            model.addAttribute("proveedores", proveedorService.findAllActivos());
            return "productos/crear";
        }

        if (producto.getPrecioUnitario().compareTo(BigDecimal.ZERO) <= 0) {
            model.addAttribute("error", "Precio unitario no puede ser cero o menor a cero.");
            model.addAttribute("producto", producto);
            model.addAttribute("unidades", unidadMedidaService.findAllActivos());
            model.addAttribute("active_page", "producto");
            model.addAttribute("sucursales", sucursalService.findAllActivos());
            model.addAttribute("categorias", categoriaProductoService.findAllActivos());
            model.addAttribute("proveedores", proveedorService.findAllActivos());
            return "productos/crear";
        }

        // Guardar cookie con la categoría seleccionada
        if (producto.getCategoria() != null) {
            Cookie categoriaCookie = new Cookie("categoria_id", producto.getCategoria().getId().toString());
            categoriaCookie.setMaxAge(60 * 60); // 1 hora
            categoriaCookie.setPath("/"); // visible para toda la app
            response.addCookie(categoriaCookie);
        }
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
            model.addAttribute("sucursales", sucursalService.findAllActivos());
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

    private String generarCodigoProducto() {
        long numero = productoService.count() + 1;
        return String.format("PRD-%05d", numero);
    }
}
