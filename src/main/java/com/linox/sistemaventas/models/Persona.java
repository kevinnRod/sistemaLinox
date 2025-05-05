package com.linox.sistemaventas.models;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "persona")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Persona {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_persona")
    private Integer idPersona;

    @Column(name = "dni", length = 8, nullable = false, unique = true)
    private String dni;

    @Column(name = "nombres", length = 40, nullable = false)
    private String nombres;

    @Column(name = "apellidos", length = 50, nullable = false)
    private String apellidos;

    @Column(name = "fecha_nacimiento")
    private LocalDate fechaNacimiento;

    @Column(name = "genero", length = 1)
    private Character genero; // O String(1)

    @Column(name = "correo", length = 50, unique = true)
    private String correo;

    @Column(name = "telefono", length = 20)
    private String telefono;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "id_estado")
    private Integer idEstado; // FK a Estado?

    // Relación OneToOne con Usuario (dueño de la relación)
    @OneToOne(fetch = FetchType.LAZY, optional = true) // optional=true si una persona puede no tener usuario
    @JoinColumn(name = "id_usuario", referencedColumnName = "id_usuario", unique = true)
    private Usuario usuario;
}