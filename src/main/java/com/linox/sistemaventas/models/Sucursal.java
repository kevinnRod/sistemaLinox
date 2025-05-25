package com.linox.sistemaventas.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor; // Opcional
import lombok.ToString; // Opcional
import lombok.EqualsAndHashCode; // Opcional, pero recomendado para entidades JPA

import java.time.LocalDateTime;

@Entity
@Table(name = "sucursal")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor // Opcional
@ToString(exclude = "empresaAnfitrion") // Excluir la relación ManyToOne para evitar bucles
@EqualsAndHashCode(of = "idSucursal") // Genera equals y hashCode basado en la PK para un buen funcionamiento con
                                      // colecciones
public class Sucursal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_sucursal")
    private Integer idSucursal;

    @Column(name = "nombre_sucursal", length = 40, nullable = false)
    private String nombreSucursal;

    @Column(length = 50)
    private String direccion;

    @Column(length = 20)
    private String telefono;

    @Column(length = 50)
    private String email;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "id_estado")
    private Integer idEstado;

    @ManyToOne
    @JoinColumn(name = "id_empresa", nullable = false)
    private EmpresaAnfitrion empresaAnfitrion;

    // No se necesitan getters/setters manuales ni constructor vacío si usas Lombok
}