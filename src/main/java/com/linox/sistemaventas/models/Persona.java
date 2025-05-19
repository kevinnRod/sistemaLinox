package com.linox.sistemaventas.models;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "persona") // ✅ Tabla única para toda la jerarquía
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo_persona", discriminatorType = DiscriminatorType.STRING)
@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class Persona {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_persona")
    private Integer idPersona;

    @Column(length = 8, nullable = false, unique = true)
    private String dni;

    @Column(length = 40, nullable = false)
    private String nombres;

    @Column(length = 50, nullable = false)
    private String apellidos;

    private LocalDate fechaNacimiento;

    @Column(length = 1)
    private Character genero;

    @Column(length = 50, unique = true)
    private String correo;

    @Column(length = 20)
    private String telefono;

    @Column(length = 100)
    private String direccion;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Column(name = "id_estado")
    private Integer idEstado;

}
