package com.linox.sistemaventas.services.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.linox.sistemaventas.models.DetalleVenta;
import com.linox.sistemaventas.models.Kardex;
import com.linox.sistemaventas.models.Producto;
import com.linox.sistemaventas.models.TipoMovimiento;
import com.linox.sistemaventas.models.Usuario;
import com.linox.sistemaventas.models.Venta;
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

}
