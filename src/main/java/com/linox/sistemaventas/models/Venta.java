package com.linox.sistemaventas.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "venta")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode(of = "idVenta")
public class Venta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_venta")
    private Integer idVenta;

    @Column(name = "cod_venta", length = 30, nullable = false, unique = true)
    private String codVenta;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "update_at")
    private LocalDateTime updatedAt;

    @Column(name = "id_estado")
    private Integer idEstado;

    @ManyToOne
    @JoinColumn(name = "id_sucursal", nullable = false)
    private Sucursal sucursal;

    @ManyToOne
    @JoinColumn(name = "id_persona", nullable = false)
    private Persona persona;

    @ManyToOne
    @JoinColumn(name = "cod_cliente", referencedColumnName = "cod_cliente")
    private Cliente cliente;
    

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
