package com.linox.sistemaventas.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;

@Entity
@Table(name = "empresa_anfitrion")
@PrimaryKeyJoinColumn(name = "id_empresa") // hereda la PK de Empresa
public class EmpresaAnfitrion extends Empresa {

    @Column(length = 255)
    private String logo;

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }
}
