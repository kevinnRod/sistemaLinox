package com.linox.sistemaventas.models;

import jakarta.persistence.*;

@Entity
@Table(name = "cliente_juridico")
@DiscriminatorValue("juridico")
public class ClienteJuridico extends Cliente {

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_empresa", referencedColumnName = "id_empresa")
    private Empresa empresa;

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }
}
