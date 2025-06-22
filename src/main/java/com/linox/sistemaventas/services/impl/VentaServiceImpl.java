package com.linox.sistemaventas.services.impl;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.linox.sistemaventas.models.DetalleVenta;
import com.linox.sistemaventas.models.Kardex;
import com.linox.sistemaventas.models.Producto;
import com.linox.sistemaventas.models.TipoMovimiento;
import com.linox.sistemaventas.models.Usuario;
import com.linox.sistemaventas.models.Venta;
import com.linox.sistemaventas.repositories.DetalleVentaRepository;
import com.linox.sistemaventas.repositories.VentaRepository;
import com.linox.sistemaventas.services.DetalleVentaService;
import com.linox.sistemaventas.services.KardexService;
import com.linox.sistemaventas.services.ProductoService;
import com.linox.sistemaventas.services.TipoMovimientoService;
import com.linox.sistemaventas.services.UsuarioService;
import com.linox.sistemaventas.services.VentaService;

@Service
public class VentaServiceImpl implements VentaService {

    private final VentaRepository ventaRepository;

    @Autowired
    private ProductoService productoService;

    @Autowired
    private KardexService kardexService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private TipoMovimientoService tipoMovimientoService;

    @Autowired
    private DetalleVentaRepository detalleVentaRepository;

    @Autowired
    private DetalleVentaService detalleVentaService;

    public VentaServiceImpl(VentaRepository ventaRepository) {
        this.ventaRepository = ventaRepository;
    }

    @Override
    public List<Venta> findAllVentas() {
        return ventaRepository.findAll();
    }

    @Override
    public List<Venta> findAllActiveVentas() {
        return ventaRepository.findByIdEstado(1);
    }

    @Override
    public Optional<Venta> findVentaById(Integer id) {
        return ventaRepository.findById(id);
    }

    @Override
    public Venta saveVenta(Venta venta) {
        // Aquí puedes agregar lógica de negocio adicional si es necesario
        return ventaRepository.save(venta);
    }

    @Override
    public void softDeleteVenta(String id) {
        Optional<Venta> ventaOptional = ventaRepository.findByCodVenta(id);

        if (ventaOptional.isPresent()) {

            Venta venta = ventaOptional.get();
            venta.setIdEstado(0); // Marcar la venta como eliminada
            ventaRepository.save(venta);

            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String username = auth.getName();
            Usuario usuario = usuarioService.findByUsuario(username).get(); // Implementa este método según tu seguridad

            // Obtener detalles y actualizarlos
            List<DetalleVenta> detalles = venta.getDetallesVenta();
            for (DetalleVenta detalle : detalles) {
                detalle.setIdEstado(0); // Marcar detalle como eliminado
                detalleVentaService.saveDetalleVenta(detalle);

                // Actualizar el stock del producto (sumar la cantidad anulada)
                Producto producto = detalle.getProducto();
                producto.setStock(producto.getStock() + detalle.getCantidad());
                productoService.save(producto);

                // Registrar el movimiento en el Kardex
                Kardex kardex = new Kardex();
                TipoMovimiento tipoMovimiento = tipoMovimientoService.findByCodigo("ENT").get(); // ING: ingreso por
                                                                                                 // anulación
                kardex.setTipoMovimiento(tipoMovimiento);
                kardex.setProducto(producto);
                kardex.setCantidad(detalle.getCantidad());
                kardex.setStockResultante(producto.getStock());
                kardex.setPrecioUnitario(producto.getPrecioUnitario());
                kardex.setDocumentoReferencia(venta.getCodVenta()); // o venta.getId()
                kardex.setObservaciones("Anulación de venta");
                kardex.setFechaMovimiento(LocalDateTime.now());
                kardex.setSucursal(producto.getSucursal());
                kardex.setIdUsuario(usuario.getIdUsuario());
                kardex.setIdEstado(1);

                kardexService.save(kardex);
            }
        }
    }

    @Override
    public void guardarVentaConDetalles(Venta venta, List<Integer> productoIds, List<Integer> cantidades) {

        venta.setFechaV(LocalDateTime.now());
        venta.setIdEstado(1);
        venta.setCreatedAt(LocalDateTime.now());
        venta.setUpdatedAt(LocalDateTime.now());

        List<DetalleVenta> detalles = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        for (int i = 0; i < productoIds.size(); i++) {
            Producto producto = productoService.findById(productoIds.get(i)).get();
            int cantidad = cantidades.get(i);
            BigDecimal subtotal = producto.getPrecioUnitario().multiply(BigDecimal.valueOf(cantidad));

            producto.setStock(producto.getStock() - cantidad);
            productoService.save(producto);
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String username = auth.getName();
            Usuario usuario = usuarioService.findByUsuario(username).get();

            Kardex kardex = new Kardex();
            TipoMovimiento tipoMovimiento = tipoMovimientoService.findByCodigo("SAL").get();
            kardex.setTipoMovimiento(tipoMovimiento);
            kardex.setProducto(producto);
            kardex.setCantidad(cantidad);
            kardex.setStockResultante(producto.getStock());
            kardex.setPrecioUnitario(producto.getPrecioUnitario());
            kardex.setDocumentoReferencia(venta.getCodVenta());
            kardex.setObservaciones("Se realizó una venta");
            kardex.setFechaMovimiento(venta.getFechaV());
            kardex.setSucursal(producto.getSucursal());
            kardex.setIdUsuario(usuario.getIdUsuario());
            kardex.setIdEstado(1);
            kardexService.save(kardex);

            DetalleVenta detalle = new DetalleVenta();
            detalle.setVenta(venta);
            detalle.setProducto(producto);
            detalle.setCantidad(cantidad);
            detalle.setSubtotal(subtotal);
            detalle.setIdEstado(1);
            detalle.setCreatedAt(LocalDateTime.now());
            detalle.setUpdatedAt(LocalDateTime.now());

            total = total.add(subtotal);
            detalles.add(detalle);
        }

        venta.setTotal(total);
        venta.setDetallesVenta(detalles);
        ventaRepository.save(venta); // requiere cascade en Venta -> DetalleVenta
    }

    @Override
    public long count() {
        return ventaRepository.count();
    }

    @Override
    public Optional<Venta> findByCodVenta(String codVenta) {
        return ventaRepository.findByCodVenta(codVenta);
    }

    @Override
    public List<Object[]> obtenerVentasPorMes() {
        return ventaRepository.obtenerVentasPorMes();
    }

    @Override
    public List<Object[]> obtenerTop10ProductosMasVendidosDelMes() {
        return detalleVentaRepository.obtenerTop10ProductosMasVendidosDelMes();
    }

    @Override
    public List<Object[]> obtenerProductosConMayorImporteUltimoMes() {
        return detalleVentaRepository.obtenerProductosConMayorImporteUltimoMes(PageRequest.of(0, 5));
    }

    @Override
    public List<Object[]> obtenerTotalesPorMes(Integer idEmpleado) {
        return ventaRepository.obtenerTotalesPorMes(idEmpleado);
    }

    @Override
    public long contarVentasHoy() {
        LocalDateTime inicio = LocalDate.now().atStartOfDay();
        LocalDateTime fin = LocalDateTime.now();
        return ventaRepository.countByFechaVBetweenAndIdEstado(inicio, fin, 1);
    }

    @Override
    public BigDecimal calcularTotalVentasHoy() {
        LocalDateTime inicio = LocalDate.now().atStartOfDay();
        LocalDateTime fin = LocalDateTime.now();
        return ventaRepository.sumTotalByFechaVBetweenAndIdEstado(inicio, fin, 1).orElse(BigDecimal.ZERO);
    }

    @Override
    public List<Integer> obtenerVentasPorHoraHoy() {
        LocalDate hoy = LocalDate.now();
        LocalDateTime inicio = hoy.atStartOfDay();
        LocalDateTime fin = hoy.atTime(LocalTime.MAX);
        return ventasPorHora(inicio, fin);
    }

    @Override
    public List<Integer> obtenerVentasPorHoraAyer() {
        LocalDate ayer = LocalDate.now().minusDays(1);
        LocalDateTime inicio = ayer.atStartOfDay();
        LocalDateTime fin = ayer.atTime(LocalTime.MAX);
        return ventasPorHora(inicio, fin);
    }

    @Override
    public List<String> obtenerHorasHoyYAyer() {
        return Arrays.stream(new int[24])
                .mapToObj(i -> String.format("%02d:00", i))
                .collect(Collectors.toList());
    }

    @Override
    public long contarVentasSemana() {
        LocalDate hoy = LocalDate.now();
        LocalDate inicioSemana = hoy.with(DayOfWeek.MONDAY);
        LocalDate finSemana = hoy.with(DayOfWeek.SUNDAY);

        // Convertimos a LocalDateTime para hacer coincidir con la firma del repositorio
        LocalDateTime inicio = inicioSemana.atStartOfDay();
        LocalDateTime fin = finSemana.atTime(LocalTime.MAX);

        return ventaRepository.countByFechaVBetween(inicio, fin);
    }

    @Override
    public BigDecimal calcularTotalVentasMes() {
        LocalDateTime startOfMonth = LocalDate.now().withDayOfMonth(1).atStartOfDay();
        LocalDateTime endOfMonth = LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth())
                .atTime(LocalTime.MAX);

        return ventaRepository.totalVentasEntreFechas(startOfMonth, endOfMonth)
                .orElse(BigDecimal.ZERO);
    }

    @Override
    public String obtenerProductoMasVendidoNombre() {
        List<String> productos = ventaRepository.obtenerProductoMasVendidoNombre(PageRequest.of(0, 1));
        return productos.isEmpty() ? "Sin ventas" : productos.get(0);
    }

    @Override
    public Integer obtenerProductoMasVendidoCantidad() {
        return ventaRepository.obtenerProductoMasVendidoCantidad(PageRequest.of(0, 1))
                .stream()
                .findFirst()
                .orElse(0);
    }

    private List<Integer> ventasPorHora(LocalDateTime inicio, LocalDateTime fin) {
        List<Venta> ventas = ventaRepository.findByFechaVBetweenAndIdEstado(inicio, fin, 1);
        Map<Integer, Integer> porHora = new TreeMap<>();

        for (int i = 0; i < 24; i++) {
            porHora.put(i, 0);
        }

        for (Venta v : ventas) {
            int hora = v.getFechaV().getHour();
            porHora.put(hora, porHora.get(hora) + 1);
        }

        return new ArrayList<>(porHora.values());
    }

}
