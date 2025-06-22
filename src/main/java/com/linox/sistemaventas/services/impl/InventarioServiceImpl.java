package com.linox.sistemaventas.services.impl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.linox.sistemaventas.models.Kardex;
import com.linox.sistemaventas.models.Producto;
import com.linox.sistemaventas.models.Sucursal;
import com.linox.sistemaventas.repositories.KardexRepository;
import com.linox.sistemaventas.repositories.ProductoRepository;
import com.linox.sistemaventas.services.InventarioService;

@Service
public class InventarioServiceImpl implements InventarioService {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private KardexRepository kardexRepository;

    @Override
    public List<Producto> obtenerTodosLosProductos() {
        return productoRepository.findByIdEstado(1);
    }

    @Override
    public List<Producto> obtenerProductosConBajoStock() {
        return productoRepository.findProductosConBajoStock();
    }

    @Override
    public List<Kardex> obtenerUltimosMovimientos() {
        return kardexRepository.findTop10ByIdEstadoOrderByFechaMovimientoDesc(1);
    }

    @Override
    public int contarProductosActivos() {
        return productoRepository.countByIdEstado(1);
    }

    @Override
    public int contarProductosConBajoStock() {
        return productoRepository.countByStockLessThanAndIdEstado(60, 1);
    }

    @Override
    public Map<String, Integer> obtenerStockTotalPorSucursal() {
        List<Producto> productos = productoRepository.findByIdEstado(1);
        return productos.stream()
                .collect(Collectors.groupingBy(
                        p -> p.getSucursal().getNombreSucursal(),
                        Collectors.summingInt(Producto::getStock)));
    }

    @Override
    public List<Producto> obtenerProductosAgrupadosPorSucursal() {
        // Aquí puedes aplicar lógica adicional si deseas agrupar, filtrar, ordenar,
        // etc.
        return productoRepository.findByIdEstado(1); // Solo productos activos por ejemplo
    }

    public Map<Sucursal, List<Producto>> obtenerProductosAgrupadosPorSucursalComoMapa() {
        List<Producto> productos = productoRepository.findByIdEstado(1);
        return productos.stream()
                .collect(Collectors.groupingBy(Producto::getSucursal));
    }
}
