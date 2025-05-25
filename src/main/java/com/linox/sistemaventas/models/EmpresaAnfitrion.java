package com.linox.sistemaventas.models;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor; // Importar AllArgsConstructor (opcional, si necesitas un constructor con todos los args)
import lombok.Getter; // Importar Getter
import lombok.NoArgsConstructor; // Importar NoArgsConstructor
import lombok.Setter; // Importar Setter
import lombok.ToString; // Importar ToString (opcional)

@Entity
@Table(name = "empresa_anfitrion")
@PrimaryKeyJoinColumn(name = "id_empresa")
@Getter // Genera todos los getters
@Setter // Genera todos los setters
@NoArgsConstructor // Genera un constructor sin argumentos
@AllArgsConstructor // Genera un constructor con todos los argumentos (útil para pruebas o mapeos)
@ToString(exclude = "sucursales") // Genera toString, excluyendo la lista de sucursales para evitar bucles
                                  // infinitos
public class EmpresaAnfitrion extends Empresa { // Asumiendo que Empresa es la superclase

    @Column(length = 255)
    private String logoUrl;

    @OneToMany(mappedBy = "empresaAnfitrion", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Sucursal> sucursales = new ArrayList<>();

    // Métodos de conveniencia para añadir y remover sucursales (estos no se reducen
    // con Lombok)
    public void addSucursal(Sucursal sucursal) {
        sucursales.add(sucursal);
        sucursal.setEmpresaAnfitrion(this);
    }

    public void removeSucursal(Sucursal sucursal) {
        sucursales.remove(sucursal);
        sucursal.setEmpresaAnfitrion(null);
    }
}