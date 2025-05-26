package com.linox.sistemaventas.models;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DocumentoReferencia {
    private String tipo; // Ej: "VENTA", "PEDIDO"
    private Integer id;  // ID del documento referenciado
    private String numero; // Ej: "F001-000123" (opcional)
}
