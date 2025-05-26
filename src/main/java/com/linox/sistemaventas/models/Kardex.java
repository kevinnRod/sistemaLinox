package com.linox.sistemaventas.models;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.linox.sistemaventas.utils.DocumentoReferenciaConverter;

@Entity
@Table(name = "kardex")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode(of = "idKardex")
public class Kardex {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_kardex")
    private Integer idKardex;

    @ManyToOne
    @JoinColumn(name = "id_tipo_movimiento")
    private TipoMovimiento tipoMovimiento;

    @ManyToOne
    @JoinColumn(name = "id_producto")
    private Producto producto;

    @Column(precision = 10, scale = 3)
    private BigDecimal cantidad;

    @Column(name = "stock_resultante", precision = 10, scale = 3)
    private BigDecimal stockResultante;

    @Column(name = "precio_unitario", precision = 10, scale = 2)
    private BigDecimal precioUnitario;

    @Convert(converter = DocumentoReferenciaConverter.class)
    @Column(name = "documento_referencia", columnDefinition = "json")
    private DocumentoReferencia documentoReferencia;

    @Column(length = 100)
    private String observaciones;

    @Column(name = "fecha_movimiento")
    private LocalDateTime fechaMovimiento;

    @ManyToOne
    @JoinColumn(name = "id_sucursal")
    private Sucursal sucursal;

    @Column(name = "id_usuario")
    private Integer idUsuario;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "id_estado")
    private Integer idEstado;

    @ManyToOne
    @JoinColumn(name = "id_productoE")
    private Producto productoEnsamblado;

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
