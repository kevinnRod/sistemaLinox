package com.linox.sistemaventas.models;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "pedido")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode(of = "idPedido")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pedido")
    private Integer idPedido;

    @Column(name = "fech_pedido")
    private LocalDateTime fechPedido;

    @Column(name = "estado")
    private Integer estado;

    @Column(columnDefinition = "text")
    private String observaciones;

    @Column(name = "archivo_diseño", length = 255)
    private String archivoDisenio;

    @Column(name = "fecha_entrega_estimada")
    private LocalDateTime fechaEntregaEstimada;

    @Column(name = "fecha_entrega_real")
    private LocalDateTime fechaEntregaReal;

    @Column(precision = 10, scale = 2)
    private BigDecimal total;

    @Column(precision = 10, scale = 2)
    private BigDecimal inicial;

    @ManyToOne
    @JoinColumn(name = "id_persona", nullable = false)
    private Persona persona;

    @ManyToOne
    @JoinColumn(name = "cod_cliente", referencedColumnName = "cod_cliente", nullable = false)
    private Cliente cliente;

    @PrePersist
    protected void onCreate() {
        if (fechPedido == null) {
            this.fechPedido = LocalDateTime.now();
        }
    }
}
