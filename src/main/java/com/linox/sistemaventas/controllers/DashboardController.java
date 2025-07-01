package com.linox.sistemaventas.controllers;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import com.linox.sistemaventas.models.Usuario;
import com.linox.sistemaventas.models.UsuarioRol;
import com.linox.sistemaventas.repositories.UsuarioRepository;
import com.linox.sistemaventas.services.InventarioService;
import com.linox.sistemaventas.services.UsuarioRolService;
import com.linox.sistemaventas.services.VentaService;

import jakarta.servlet.http.HttpSession;

@Controller
public class DashboardController {

    @Autowired
    private UsuarioRepository usuarioRepo;

    @Autowired
    private UsuarioRolService usuarioRolService;

    @Autowired
    private VentaService ventaService;

    @Autowired
    private InventarioService inventarioService;

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        Optional<Usuario> usuarioOpt = usuarioRepo.findByUsuario(username);
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();

            // Obtener solo roles activos
            List<UsuarioRol> rolesActivos = usuarioRolService.findAllByEstadoActivo(usuario.getIdUsuario());
            List<String> roles = rolesActivos.stream()
                    .map(rol -> rol.getRol().getNombreRol())
                    .collect(Collectors.toList());

            // Guardar en sesión
            session.setAttribute("roles", roles);

            // Para depurar:
            System.out.println("Roles en sesión: " + session.getAttribute("roles"));
        }

        return "redirect:/inicio";
    }

    @GetMapping("/inicio")
    public String inicio(Model model) {
        model.addAttribute("active_page", "inicio");

        // Obtener ventas por mes
        List<Object[]> ventasPorMes = ventaService.obtenerVentasPorMes();

        List<String> meses = new ArrayList<>();
        List<BigDecimal> totales = new ArrayList<>();

        for (Object[] fila : ventasPorMes) {
            Integer mes = (Integer) fila[0]; // 1 = enero
            BigDecimal total = (BigDecimal) fila[1];

            String nombreMes = Month.of(mes).getDisplayName(TextStyle.FULL, new Locale("es"));
            meses.add(nombreMes);
            totales.add(total);
        }

        // Obtener la predicción desde la API Flask
        Double prediccion = null;
        try {
            RestTemplate restTemplate = new RestTemplate();
            String url = "http://localhost:5000/api/prediccion-mensual";
            Map<String, Object> respuesta = restTemplate.getForObject(url, Map.class);

            if (respuesta != null) {
                prediccion = ((Number) respuesta.get("prediccion")).doubleValue();
                model.addAttribute("ventaPredicha", prediccion);
            }

        } catch (Exception e) {
            System.out.println("❌ Error al conectar con la API de predicción: " + e.getMessage());
        }

        model.addAttribute("meses", meses);
        model.addAttribute("totales", totales);

        // Top productos por cantidad
        List<Object[]> topProductos = ventaService.obtenerTop10ProductosMasVendidosDelMes();
        List<String> nombresProductos = new ArrayList<>();
        List<Integer> cantidadesVendidas = new ArrayList<>();

        for (Object[] fila : topProductos) {
            nombresProductos.add((String) fila[0]);
            cantidadesVendidas.add(((Number) fila[1]).intValue());
        }

        // Productos con mayor importe
        List<Object[]> productosConMayorImporte = ventaService.obtenerProductosConMayorImporteUltimoMes();
        List<String> nombresImporte = new ArrayList<>();
        List<Double> importes = new ArrayList<>();

        for (Object[] fila : productosConMayorImporte) {
            nombresImporte.add((String) fila[0]);
            importes.add(((Number) fila[1]).doubleValue());
        }

        model.addAttribute("nombresImporte", nombresImporte);
        model.addAttribute("importes", importes);
        model.addAttribute("nombresProductos", nombresProductos);
        model.addAttribute("cantidadesVendidas", cantidadesVendidas);

        return "hola";
    }

    @GetMapping("/dashboard/venta")
    public String mostrarDashboardVenta(Model model, HttpSession session) {
        List<String> roles = (List<String>) session.getAttribute("roles");
        if (roles == null || !roles.contains("ADMIN")) {
            return "redirect:/acceso-denegado";
        }

        // KPI Ventas
        long ventasHoy = ventaService.contarVentasHoy();
        BigDecimal montoHoy = ventaService.calcularTotalVentasHoy();
        long ventasSemana = ventaService.contarVentasSemana();
        BigDecimal montoMes = ventaService.calcularTotalVentasMes();

        // Gráfico de ventas por hora
        List<String> horas = IntStream.rangeClosed(5, 20)
                .mapToObj(h -> String.format("%02d:00", h))
                .collect(Collectors.toList());

        List<Integer> ventasSemanaActual = ventaService.obtenerVentasPorHoraSemanaActual().subList(5, 21);
        List<Integer> ventasSemanaPasada = ventaService.obtenerVentasPorHoraSemanaPasada().subList(5, 21);

        // Producto más vendido
        String productoTop = ventaService.obtenerProductoMasVendidoNombre();
        long cantidadTop = ventaService.obtenerProductoMasVendidoCantidad();

        // ✅ Ventas por cliente (nuevo)
        List<Object[]> ventasCliente = ventaService.obtenerVentasPorCliente();
        List<String> clientes = new ArrayList<>();
        List<BigDecimal> montosCliente = new ArrayList<>();

        for (Object[] fila : ventasCliente) {
            clientes.add((String) fila[0]);
            montosCliente.add((BigDecimal) fila[1]);
        }

        // Agregar atributos al modelo
        model.addAttribute("active_page", "dventa");
        model.addAttribute("ventasHoy", ventasHoy);
        model.addAttribute("montoHoy", montoHoy);
        model.addAttribute("ventasSemana", ventasSemana);
        model.addAttribute("montoMes", montoMes);
        model.addAttribute("horas", horas);
        model.addAttribute("ventasSemanaActual", ventasSemanaActual);
        model.addAttribute("ventasSemanaPasada", ventasSemanaPasada);
        model.addAttribute("productoTop", productoTop);
        model.addAttribute("cantidadTop", cantidadTop);

        // Nuevos atributos para ventas por cliente
        model.addAttribute("clientes", clientes);
        model.addAttribute("montosCliente", montosCliente);

        return "dashboard/venta";
    }

    @GetMapping("/api/ventas/por-cliente")
    @ResponseBody
    public List<Map<String, Object>> ventasPorClientePorRango(
            @RequestParam String inicio,
            @RequestParam String fin) {

        List<Object[]> resultados = ventaService.obtenerVentasPorClienteEnRango(LocalDate.parse(inicio),
                LocalDate.parse(fin));

        return resultados.stream()
                .map(obj -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("cliente", obj[0]);
                    map.put("monto", obj[1]);
                    return map;
                }).collect(Collectors.toList());
    }

}
