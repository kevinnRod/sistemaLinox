
package com.linox.sistemaventas.controllers;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.linox.sistemaventas.models.Cliente;
import com.linox.sistemaventas.models.ClienteJuridico;
import com.linox.sistemaventas.models.ClienteNatural;
import com.linox.sistemaventas.models.Empleado;
import com.linox.sistemaventas.models.Venta;
import com.linox.sistemaventas.services.ClienteService;
import com.linox.sistemaventas.services.EmpleadoService;
import com.linox.sistemaventas.services.ProductoService;
import com.linox.sistemaventas.services.VentaService;

@Controller
@RequestMapping("/ventas")
public class VentaController {

    @Autowired
    private VentaService ventaService;

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private EmpleadoService empleadoService;

    @Autowired
    private ProductoService productoService;

    // 3. Mostrar listado
    @GetMapping()
    public String listarVentas(Model model) {
        List<Venta> ventas = ventaService.findAllActiveVentas();

        List<Map<String, Object>> datosVentas = ventas.stream().map(venta -> {
            Map<String, Object> datos = new HashMap<>();
            datos.put("codigo", venta.getCodVenta());
            datos.put("id", venta.getIdVenta());
            datos.put("total", venta.getTotal());
            datos.put("fecha", venta.getFechaV().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
            datos.put("empleado", venta.getEmpleado().getNombres());
            Cliente cliente = clienteService.findById(venta.getCliente().getCodCliente())
                    .orElseThrow(() -> new RuntimeException(
                            "Cliente no encontrado con ID: " + venta.getCliente().getCodCliente()));
            if (cliente instanceof ClienteNatural cn) {
                datos.put("nombre", cn.getPersona().getNombres() + " " + cn.getPersona().getApellidos());
                datos.put("identificacion", cn.getPersona().getDni());
            } else if (cliente instanceof ClienteJuridico cj) {
                datos.put("nombre", cj.getEmpresa().getRazonSocial());
                datos.put("identificacion", cj.getEmpresa().getRuc());
            }

            return datos;
        }).toList();

        model.addAttribute("ventas", datosVentas);
        model.addAttribute("active_page", "listarventa");
        return "venta/listar";
    }

    // Mostrar formulario de creación
    @GetMapping("/crear")
    public String mostrarFormulario(Model model) {
        Venta venta = new Venta();
        venta.setCodVenta(generarCodigoVenta());
        model.addAttribute("venta", venta);
        model.addAttribute("clientes", clienteService.findAllActivos());
        model.addAttribute("empleados", empleadoService.listarTodos());
        model.addAttribute("productos", productoService.findAllActivos());
        model.addAttribute("active_page", "listarventa");
        return "venta/crear";
    }

    @PostMapping("/guardar")
    public String guardarVenta(@ModelAttribute Venta venta,
            @RequestParam("productoIds") List<Integer> productoIds,
            @RequestParam("cantidades") List<Integer> cantidades,
            @RequestParam("codCliente") String codCliente,
            @RequestParam("empleado.id") Integer empleadoId,
            RedirectAttributes redirectAttrs) {
        Optional<Cliente> optionalCliente = clienteService.findById(codCliente);
        Optional<Empleado> optionalEmpleado = empleadoService.findById(empleadoId);

        if (optionalCliente.isPresent() && optionalEmpleado.isPresent()) {
            Cliente cliente = optionalCliente.get();
            Empleado empleado = optionalEmpleado.get();

            venta.setCliente(cliente);
            venta.setEmpleado(empleado);

            ventaService.guardarVentaConDetalles(venta, productoIds, cantidades);
            return "redirect:/ventas";
        } else {
            redirectAttrs.addFlashAttribute("error", "Cliente o empleado no encontrado.");
            return "redirect:/ventas/crear";
        }

    }

    // Mostrar formulario de edición

    // Actualizar venta
    @PostMapping("/update")
    public String actualizar(@ModelAttribute Venta venta) {
        Optional<Venta> original = ventaService.findVentaById(venta.getIdVenta());
        if (original.isPresent()) {
            venta.setCreatedAt(original.get().getCreatedAt());
            ventaService.saveVenta(venta);
        }
        return "redirect:/ventas";
    }

    // Eliminación lógica
    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable String id) {
        ventaService.softDeleteVenta(id);
        return "redirect:/ventas";
    }

    @GetMapping("/{codigo}")
    public String verDetalleVenta(@PathVariable("codigo") String codigo, Model model) {
        // Buscar la venta por código, con su detalle (productos, cantidades, precios,
        // etc.)
        Optional<Venta> venta = ventaService.findByCodVenta(codigo);

        if (venta.isPresent() == false) {
            // Manejar error o redirigir
            return "redirect:/ventas";
        }
        Cliente cliente = clienteService.findById(venta.get().getCliente().getCodCliente())
                .orElseThrow(() -> new RuntimeException(
                        "Cliente no encontrado con ID: " + venta.get().getCliente().getCodCliente()));
        if (cliente instanceof ClienteNatural cn) {
            model.addAttribute("nombre", cn.getPersona().getNombres() + " " + cn.getPersona().getApellidos());
            model.addAttribute("identificacion", cn.getPersona().getDni());
        } else if (cliente instanceof ClienteJuridico cj) {
            model.addAttribute("nombre", cj.getEmpresa().getRazonSocial());
            model.addAttribute("identificacion", cj.getEmpresa().getRuc());
        }

        model.addAttribute("venta", venta.get());
        model.addAttribute("active_page", "listarventa");
        return "venta/detalle"; // nombre del template para detalle
    }

    private String generarCodigoVenta() {
        long total = ventaService.count() + 1;
        return String.format("RRD01-%05d", total);
    }
}
