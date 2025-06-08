// DetalleVentaId.java (Composite Primary Key Class)
package com.linox.sistemaventas.models;

import java.io.Serializable;
import java.util.Objects;

public class DetalleVentaId implements Serializable {

    private Integer venta; // Corresponds to the 'idVenta' field in DetalleVenta's ManyToOne 'venta'
    private Integer producto; // Corresponds to the 'id' field in DetalleVenta's ManyToOne 'producto'

    public DetalleVentaId() {
    }

    public DetalleVentaId(Integer venta, Integer producto) {
        this.venta = venta;
        this.producto = producto;
    }

    // Must override equals and hashCode for composite primary keys
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        DetalleVentaId that = (DetalleVentaId) o;
        return Objects.equals(venta, that.venta) &&
                Objects.equals(producto, that.producto);
    }

    @Override
    public int hashCode() {
        return Objects.hash(venta, producto);
    }
}
