// package com.linox.sistemaventas.services;
// VentaService.java
package com.linox.sistemaventas.services;

import java.util.List;
import java.util.Optional;

import com.linox.sistemaventas.models.Venta;

public interface VentaService {
    List<Venta> findAllVentas(); // Renamed for clarity

    List<Venta> findAllActiveVentas(); // Renamed from findAllActivas

    Optional<Venta> findVentaById(Integer id); // Renamed from findById

    Optional<Venta> findByCodVenta(String codVenta); // Renamed from findById

    Venta saveVenta(Venta venta); // Consolidated method for saving/updating with all business logic

    void softDeleteVenta(String id); // Renamed from deleteLogico for clarity

    void guardarVentaConDetalles(Venta venta, List<Integer> productoIds, List<Integer> cantidades);

    long count();

}