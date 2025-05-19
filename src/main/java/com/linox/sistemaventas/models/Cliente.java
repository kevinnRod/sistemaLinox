package com.linox.sistemaventas.models;

import jakarta.persistence.*;

@Entity
public class Cliente extends Persona {

    @Column(name = "cod_cliente", length = 20, nullable = false)
    private String codigoCliente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_categoria_cliente", referencedColumnName = "idCategoria", nullable = false)
    private CategoriaCliente categoriaCliente;

    // Getters y Setters
    public String getCodigoCliente() {
        return codigoCliente;
    }

    public void setCodigoCliente(String codigoCliente) {
        this.codigoCliente = codigoCliente;
    }

    public CategoriaCliente getCategoriaCliente() {
        return categoriaCliente;
    }

    public void setCategoriaCliente(CategoriaCliente categoriaCliente) {
        this.categoriaCliente = categoriaCliente;
    }
}
