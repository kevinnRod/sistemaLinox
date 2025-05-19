package com.linox.sistemaventas.models;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("CLIENTE_JURIDICO")
public class ClienteJuridico extends Cliente {

    @Column(name = "ruc", length = 11)
    private String ruc;

    // Getters y Setters
    public String getRuc() {
        return ruc;
    }

    public void setRuc(String ruc) {
        this.ruc = ruc;
    }
}
