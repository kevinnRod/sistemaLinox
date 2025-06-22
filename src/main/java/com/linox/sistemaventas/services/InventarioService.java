package com.linox.sistemaventas.services;

import java.util.List;
import java.util.Map;

import com.linox.sistemaventas.models.Kardex;
import com.linox.sistemaventas.models.Producto;

public interface InventarioService {
    List<Producto> obtenerTodosLosProductos();

    List<Producto> obtenerProductosConBajoStock();

    List<Kardex> obtenerUltimosMovimientos();

    int contarProductosActivos();

    int contarProductosConBajoStock();

    Map<String, Integer> obtenerStockTotalPorSucursal();

    List<Producto> obtenerProductosAgrupadosPorSucursal();
}
