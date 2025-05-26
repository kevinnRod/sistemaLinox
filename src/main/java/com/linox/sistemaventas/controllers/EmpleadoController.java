package com.linox.sistemaventas.controllers;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.linox.sistemaventas.models.Cargo;
import com.linox.sistemaventas.models.Empleado;
import com.linox.sistemaventas.models.Sucursal;
import com.linox.sistemaventas.services.CargoService;
import com.linox.sistemaventas.services.EmpleadoService;
import com.linox.sistemaventas.services.SucursalService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/empleado")
public class EmpleadoController {

    @Autowired
    private EmpleadoService empleadoService;

    @Autowired
    private CargoService cargoService;

    @Autowired
    private SucursalService sucursalService;

    @GetMapping
    public String getAll(Model model) {
        List<Empleado> empleados = empleadoService.findAllByEstadoActivo();
        model.addAttribute("empleados", empleados);
        model.addAttribute("active_page", "empleado");
        return "empleado/empleados";
    }

    @GetMapping("/create")
    public String crearEmpleado(Model model) {
        List<Cargo> cargos = cargoService.findAllActivos();
        List<Sucursal> sucursales = sucursalService.findAllByEstadoActivo();

        model.addAttribute("cargos", cargos);
        model.addAttribute("sucursales", sucursales);
        model.addAttribute("active_page", "empleado");
        return "empleado/crearEmpleado";
    }

    @PostMapping("/save")
    public String saveEmpleado(
            @RequestParam String dni,
            @RequestParam String nombres,
            @RequestParam String apellidos,
            @RequestParam String correo,
            @RequestParam String telefono,
            @RequestParam String direccion,
            @RequestParam Integer idCargo,
            @RequestParam Integer idSucursal,
            HttpServletRequest request,
            RedirectAttributes redirectAttributes) {
        try {
            Map<String, String> errores = new HashMap<>();

            // Validaciones básicas
            if (dni == null || !dni.matches("\\d{8}")) {
                errores.put("dni", "El DNI debe contener exactamente 8 dígitos numéricos");
            } else {
                // Validar que el DNI sea único
                boolean existeDni = empleadoService.existsByDni(dni);
                if (existeDni) {
                    errores.put("dni", "El DNI ya está registrado");
                }
            }

            if (nombres == null || nombres.trim().isEmpty()) {
                errores.put("nombres", "El campo nombres es obligatorio");
            }
            if (apellidos == null || apellidos.trim().isEmpty()) {
                errores.put("apellidos", "El campo apellidos es obligatorio");
            }
            if (correo == null || correo.trim().isEmpty() || !correo.matches(".+@.+\\..+")) {
                errores.put("correo", "Ingrese un correo válido");
            }

            if (!errores.isEmpty()) {
                // Convertir request params a Map<String,String> para repoblar form
                Map<String, String> datos = new HashMap<>();
                datos.put("dni", dni);
                datos.put("nombres", nombres);
                datos.put("apellidos", apellidos);
                datos.put("correo", correo);
                datos.put("telefono", telefono);
                datos.put("direccion", direccion);
                datos.put("idCargo", idCargo.toString());
                datos.put("idSucursal", idSucursal.toString());

                redirectAttributes.addFlashAttribute("errores", errores);
                redirectAttributes.addFlashAttribute("datos", datos);
                return "redirect:/empleado/create";
            }

            Empleado empleado = new Empleado();
            empleado.setDni(dni);
            empleado.setNombres(nombres);
            empleado.setApellidos(apellidos);
            empleado.setCorreo(correo);
            empleado.setTelefono(telefono);
            empleado.setDireccion(direccion);
            empleado.setIdEstado(1);

            // Generar código de empleado automático
            String nuevoCodigo = generarCodigoEmpleado();
            empleado.setCodEmpleado(nuevoCodigo);

            empleado.setCreatedAt(LocalDateTime.now());
            empleado.setUpdatedAt(LocalDateTime.now());

            Cargo cargo = cargoService.findById(idCargo).orElse(null);
            Sucursal sucursal = sucursalService.findById(idSucursal).orElse(null);
            empleado.setCargo(cargo);
            empleado.setSucursal(sucursal);

            empleadoService.save(empleado);
            redirectAttributes.addFlashAttribute("success", "Empleado guardado correctamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al guardar el empleado: " + e.getMessage());
            return "redirect:/empleado/create";
        }
        return "redirect:/empleado";
    }

    @GetMapping("/editar/{id}")
    public String editarEmpleado(@PathVariable Integer id, Model model) {
        Optional<Empleado> empleadoOpt = empleadoService.findById(id);
        if (!empleadoOpt.isPresent()) {
            return "redirect:/empleado";
        }

        model.addAttribute("empleado", empleadoOpt.get());
        model.addAttribute("cargos", cargoService.findAllActivos());
        model.addAttribute("sucursales", sucursalService.findAllByEstadoActivo());
        model.addAttribute("active_page", "empleado");

        return "empleado/editarEmpleado";
    }

    @PostMapping("/actualizar/{id}")
    public String actualizarEmpleado(
            @PathVariable Integer id,
            @RequestParam String dni,
            @RequestParam String nombres,
            @RequestParam String apellidos,
            @RequestParam String correo,
            @RequestParam String telefono,
            @RequestParam String direccion,
            @RequestParam Integer idCargo,
            @RequestParam Integer idSucursal,
            RedirectAttributes redirectAttributes) {

        try {
            Optional<Empleado> empleadoOpt = empleadoService.findById(id);
            if (!empleadoOpt.isPresent()) {
                redirectAttributes.addFlashAttribute("error", "Empleado no encontrado.");
                return "redirect:/empleado";
            }

            Map<String, String> errores = new HashMap<>();

            // Validar DNI: 8 dígitos numéricos
            if (dni == null || !dni.matches("\\d{8}")) {
                errores.put("dni", "El DNI debe contener exactamente 8 dígitos numéricos");
            } else {
                // Verificar que el DNI sea único excepto para este empleado
                boolean existeDni = empleadoService.existsByDni(dni);
                if (existeDni && !empleadoOpt.get().getDni().equals(dni)) {
                    errores.put("dni", "El DNI ya está registrado");
                }
            }

            // Validar campos obligatorios
            if (nombres == null || nombres.trim().isEmpty()) {
                errores.put("nombres", "El campo nombres es obligatorio");
            }
            if (apellidos == null || apellidos.trim().isEmpty()) {
                errores.put("apellidos", "El campo apellidos es obligatorio");
            }
            if (correo == null || correo.trim().isEmpty() || !correo.matches(".+@.+\\..+")) {
                errores.put("correo", "Ingrese un correo válido");
            }

            if (!errores.isEmpty()) {
                // Repoblar datos en el formulario
                Map<String, String> datos = new HashMap<>();
                datos.put("dni", dni);
                datos.put("nombres", nombres);
                datos.put("apellidos", apellidos);
                datos.put("correo", correo);
                datos.put("telefono", telefono);
                datos.put("direccion", direccion);
                datos.put("idCargo", idCargo.toString());
                datos.put("idSucursal", idSucursal.toString());

                redirectAttributes.addFlashAttribute("errores", errores);
                redirectAttributes.addFlashAttribute("datos", datos);
                return "redirect:/empleado/editar/" + id;
            }

            Empleado empleado = empleadoOpt.get();
            empleado.setDni(dni);
            empleado.setNombres(nombres);
            empleado.setApellidos(apellidos);
            empleado.setCorreo(correo);
            empleado.setTelefono(telefono);
            empleado.setDireccion(direccion);
            empleado.setUpdatedAt(LocalDateTime.now());

            Cargo cargo = cargoService.findById(idCargo).orElse(null);
            Sucursal sucursal = sucursalService.findById(idSucursal).orElse(null);
            empleado.setCargo(cargo);
            empleado.setSucursal(sucursal);

            empleadoService.save(empleado);
            redirectAttributes.addFlashAttribute("success", "Empleado actualizado correctamente.");

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al actualizar el empleado: " + e.getMessage());
            return "redirect:/empleado/editar/" + id;
        }

        return "redirect:/empleado";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminarEmpleado(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            Optional<Empleado> empleadoOpt = empleadoService.findById(id);
            if (!empleadoOpt.isPresent()) {
                redirectAttributes.addFlashAttribute("error", "Empleado no encontrado.");
                return "redirect:/empleado";
            }
            Empleado empleado = empleadoOpt.get();
            empleado.setIdEstado(0); // Estado inactivo
            empleadoService.save(empleado);
            redirectAttributes.addFlashAttribute("success", "Empleado eliminado correctamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar el empleado: " + e.getMessage());
        }
        return "redirect:/empleado";
    }

    private String generarCodigoEmpleado() {
        String ultimoCodigo = empleadoService.obtenerUltimoCodigoEmpleado();
        int numero = 1;
        if (ultimoCodigo != null && ultimoCodigo.startsWith("EMP")) {
            String numeroStr = ultimoCodigo.substring(3);
            try {
                numero = Integer.parseInt(numeroStr) + 1;
            } catch (NumberFormatException e) {
                numero = 1;
            }
        }
        return String.format("EMP%05d", numero); // Ej: EMP00001, EMP00002, ...
    }

}
