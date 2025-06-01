package com.linox.sistemaventas.controllers;

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

import com.linox.sistemaventas.models.CategoriaCliente;
import com.linox.sistemaventas.models.Cliente;
import com.linox.sistemaventas.models.ClienteJuridico;
import com.linox.sistemaventas.models.ClienteNatural;
import com.linox.sistemaventas.models.Empresa;
import com.linox.sistemaventas.models.Persona;
import com.linox.sistemaventas.services.CategoriaClienteService;
import com.linox.sistemaventas.services.ClienteService;
import com.linox.sistemaventas.services.EmpresaService;
import com.linox.sistemaventas.services.PersonaService;

@Controller
@RequestMapping("/cliente")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private CategoriaClienteService categoriaClienteService;

    @Autowired
    private PersonaService personaService;

    @Autowired
    private EmpresaService empresaService;

    // Listar clientes activos
    @GetMapping
    public String listarClientes(Model model) {
        List<Cliente> clientes = clienteService.findAllByEstadoActivo();

        List<Map<String, Object>> datosClientes = clientes.stream().map(cliente -> {
            Map<String, Object> datos = new HashMap<>();
            datos.put("codigo", cliente.getCodCliente());
            datos.put("categoria", cliente.getCategoriaCliente().getNombre());

            if (cliente instanceof ClienteNatural cn) {
                datos.put("nombre", cn.getPersona().getNombres() + " " + cn.getPersona().getApellidos());
                datos.put("identificacion", cn.getPersona().getDni());
                datos.put("telefono", cn.getPersona().getTelefono());
                datos.put("direccion", cn.getPersona().getDireccion());
                datos.put("tipo", "Natural");
            } else if (cliente instanceof ClienteJuridico cj) {
                datos.put("nombre", cj.getEmpresa().getRazonSocial());
                datos.put("identificacion", cj.getEmpresa().getRuc());
                datos.put("telefono", cj.getEmpresa().getTelefono());
                datos.put("direccion", cj.getEmpresa().getDireccion());
                datos.put("tipo", "Jurídico");
            }

            return datos;
        }).toList();

        model.addAttribute("clientes", datosClientes);
        model.addAttribute("active_page", "cliente");
        return "cliente/clientes";
    }

    // Mostrar formulario de creación
    @GetMapping("/create")
    public String crearCliente(Model model) {
        model.addAttribute("clienteNatural", new ClienteNatural());
        model.addAttribute("clienteJuridico", new ClienteJuridico());
        model.addAttribute("categorias", categoriaClienteService.findAllActivos());
        model.addAttribute("active_page", "cliente");
        return "cliente/crearCliente";
    }

    // Guardar cliente (natural o jurídico)
    @PostMapping("/save")
    public String guardarCliente(
            @RequestParam("tipo") String tipo,
            @RequestParam("nombres") String nombres,
            @RequestParam("apellidos") String apellidos,
            @RequestParam("dni") String dni,
            @RequestParam("telefono") String telefono,
            @RequestParam("direccion") String direccion,
            @RequestParam("idCategoriaCliente") Integer idCategoriaCliente,
            @RequestParam(value = "ruc", required = false) String ruc,
            @RequestParam(value = "razonSocial", required = false) String razonSocial,
            @RequestParam(value = "correo", required = false) String correo,
            @RequestParam(value = "nombreComercial", required = false) String nombreComercial,
            RedirectAttributes redirectAttributes) {

        try {
            CategoriaCliente categoria = categoriaClienteService.findById(idCategoriaCliente)
                    .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

            Cliente cliente;

            if ("JURIDICO".equalsIgnoreCase(tipo)) {
                // Validar RUC y Razón Social
                Optional<Empresa> empresaOpt = empresaService.buscarPorRuc(ruc);
                Empresa empresa;

                if (empresaOpt.isPresent()) {
                    empresa = empresaOpt.get();
                    if (clienteService.existsByEmpresa(empresa)) {
                        redirectAttributes.addFlashAttribute("error", "La empresa ya está registrada como cliente.");
                        return "redirect:/cliente";
                    }
                    // Actualizar datos si han cambiado
                    empresa.setRazonSocial(razonSocial);
                    empresa.setDireccion(direccion);
                    empresa.setTelefono(telefono);
                    empresa.setCorreo(correo);
                    empresa.setNombreComercial(nombreComercial);
                } else {
                    empresa = new Empresa();
                    empresa.setRuc(ruc);
                    empresa.setRazonSocial(razonSocial);
                    empresa.setDireccion(direccion);
                    empresa.setTelefono(telefono);
                    empresa.setCorreo(correo);
                    empresa.setNombreComercial(nombreComercial);
                    empresa.setIdEstado(1);
                }
                empresaService.guardar(empresa);

                ClienteJuridico cj = new ClienteJuridico();
                cj.setEmpresa(empresa);
                cliente = cj;

            } else {
                // Validar DNI
                Optional<Persona> personaOpt = personaService.findByDni(dni);
                Persona persona;

                if (personaOpt.isPresent()) {
                    persona = personaOpt.get();
                    if (clienteService.existsByPersona(persona)) {
                        redirectAttributes.addFlashAttribute("error", "La persona ya está registrada como cliente.");
                        return "redirect:/cliente";
                    }
                    // Actualizar datos si han cambiado
                    persona.setNombres(nombres);
                    persona.setApellidos(apellidos);
                    persona.setDireccion(direccion);
                    persona.setTelefono(telefono);
                    persona.setCorreo(correo);
                } else {
                    persona = new Persona();
                    persona.setDni(dni);
                    persona.setNombres(nombres);
                    persona.setApellidos(apellidos);
                    persona.setDireccion(direccion);
                    persona.setTelefono(telefono);
                    persona.setCorreo(correo);
                    persona.setIdEstado(1);
                }
                personaService.save(persona);

                ClienteNatural cn = new ClienteNatural();
                cn.setPersona(persona);
                cliente = cn;
            }

            cliente.setCodCliente(generarCodigoCliente());
            cliente.setCategoriaCliente(categoria);
            cliente.setIdEstado(1);

            clienteService.save(cliente);
            redirectAttributes.addFlashAttribute("success", "Cliente registrado correctamente.");

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al registrar cliente: " + e.getMessage());
        }

        return "redirect:/cliente";
    }

    // Editar cliente
    @GetMapping("/editar/{id}")
    public String editarCliente(@PathVariable String id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Cliente> clienteOpt = clienteService.findById(id);
        if (!clienteOpt.isPresent()) {
            redirectAttributes.addFlashAttribute("error", "Cliente no encontrado.");
            return "redirect:/cliente";
        }

        Cliente cliente = clienteOpt.get();
        model.addAttribute("cliente", cliente);
        model.addAttribute("categorias", categoriaClienteService.findAllActivos());
        model.addAttribute("active_page", "cliente");

        return "cliente/editarCliente";
    }

    @PostMapping("/actualizar/{id}")
    public String actualizarCliente(
            @PathVariable String id,
            @RequestParam(value = "nombres", required = false) String nombres,
            @RequestParam(value = "apellidos", required = false) String apellidos,
            @RequestParam(value = "dni", required = false) String dni,
            @RequestParam(value = "telefono", required = false) String telefono,
            @RequestParam(value = "direccion", required = false) String direccion,
            @RequestParam(value = "idCategoriaCliente", required = false) Integer idCategoriaCliente,
            @RequestParam(value = "ruc", required = false) String ruc,
            @RequestParam(value = "razonSocial", required = false) String razonSocial,
            @RequestParam(value = "correo", required = false) String correo,
            @RequestParam(value = "nombreComercial", required = false) String nombreComercial,

            RedirectAttributes redirectAttributes) {

        try {
            Optional<Cliente> clienteOpt = clienteService.findById(id);
            if (!clienteOpt.isPresent()) {
                redirectAttributes.addFlashAttribute("error", "Cliente no encontrado.");
                return "redirect:/cliente";
            }

            Cliente cliente = clienteOpt.get();
            CategoriaCliente categoria = categoriaClienteService.findById(idCategoriaCliente)
                    .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
            cliente.setCategoriaCliente(categoria);

            // Cliente Jurídico
            if (cliente instanceof ClienteJuridico) {
                Empresa empresa = ((ClienteJuridico) cliente).getEmpresa();
                empresa.setRuc(ruc);
                empresa.setRazonSocial(razonSocial);
                empresa.setDireccion(direccion);
                empresa.setTelefono(telefono);
                empresa.setCorreo(correo);
                empresa.setNombreComercial(nombreComercial);
                empresaService.guardar(empresa);
            }
            // Cliente Natural
            else if (cliente instanceof ClienteNatural) {
                Persona persona = ((ClienteNatural) cliente).getPersona();
                persona.setNombres(nombres);
                persona.setApellidos(apellidos);
                persona.setDni(dni);
                persona.setDireccion(direccion);
                persona.setTelefono(telefono);
                persona.setCorreo(correo);
                personaService.save(persona);
            }

            clienteService.save(cliente);
            redirectAttributes.addFlashAttribute("success", "Cliente actualizado correctamente.");

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al actualizar el cliente: " + e.getMessage());
        }

        return "redirect:/cliente";
    }

    // Eliminar cliente (lógico)
    @PostMapping("/eliminar/{id}")
    public String eliminarCliente(@PathVariable String id, RedirectAttributes redirectAttributes) {
        try {
            Optional<Cliente> clienteOpt = clienteService.findById(id);
            if (!clienteOpt.isPresent()) {
                redirectAttributes.addFlashAttribute("error", "Cliente no encontrado.");
                return "redirect:/cliente";
            }

            Cliente cliente = clienteOpt.get();
            cliente.setIdEstado(0); // Eliminación lógica
            clienteService.save(cliente);
            redirectAttributes.addFlashAttribute("success", "Cliente eliminado correctamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar el cliente: " + e.getMessage());
        }
        return "redirect:/cliente";
    }

    // Generador automático de código de cliente
    private String generarCodigoCliente() {
        long total = clienteService.countAll() + 1;
        return String.format("CLI-%04d", total);
    }
}
