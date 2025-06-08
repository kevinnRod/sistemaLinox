package com.linox.sistemaventas.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrimaryKeyJoinColumn; // Importar esta
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode; // Opcional, pero considera cómo lo usarás con la herencia
import lombok.Getter;
import lombok.NoArgsConstructor; // Opcional, si quieres un constructor con todos los campos
import lombok.Setter;
import lombok.ToString; // Opcional

@Entity
@Table(name = "empleado")
@PrimaryKeyJoinColumn(name = "id_persona") // Esta es la clave: indica que 'id_persona' es la PK de Empleado y FK a
                                           // Persona
@Getter
@Setter
@NoArgsConstructor
// @AllArgsConstructor // Descomentar si necesitas un constructor con todos los
// campos, incluyendo los heredados.
@ToString(callSuper = true) // Llama al toString de la clase padre
@EqualsAndHashCode(callSuper = true) // Incluye los campos de la clase padre en equals y hashCode
public class Empleado extends Persona { // Extiende de Persona

    @Column(name = "cod_empleado", length = 20)
    private String codEmpleado;

    // createdAt y updatedAt ya están en Persona, pero el diagrama los muestra
    // también en Empleado.
    // Si quieres que se gestionen a nivel de Empleado, puedes sobreescribirlos o
    // quitar los de Persona
    // y ponerlos aquí. Lo más común es que estén en la clase base para auditoría.
    // Para este ejemplo, los he omitido aquí ya que ya están en Persona.
    // @Column(name = "created_at")
    // private LocalDateTime createdAt;
    //
    // @Column(name = "updated_at")
    // private LocalDateTime updatedAt;

    // id_estado también ya está en Persona. Si el estado del empleado es diferente
    // del estado general de la persona,
    // entonces lo podrías tener aquí y en Persona. Si es el mismo, lo dejas solo en
    // Persona.
    // Para este ejemplo, lo he omitido aquí.
    // @Column(name = "id_estado")
    // private Integer idEstado;

    @ManyToOne
    @JoinColumn(name = "id_cargo", nullable = false) // FK a la tabla cargo
    private Cargo cargo;

    @ManyToOne
    @JoinColumn(name = "id_sucursal", nullable = false) // FK a la tabla sucursal
    private Sucursal sucursal;

    // Si usas @AllArgsConstructor de Lombok, no olvides que incluirá los campos de
    // Persona.
    // Si necesitas un constructor específico para Empleado que también reciba los
    // campos de Persona,
    // deberías crearlo manualmente o asegurarte de que @AllArgsConstructor de
    // Lombok haga lo que esperas
    // en clases con herencia. Un constructor manual para Empleado podría verse así:
    /*
     * public Empleado(Integer idPersona, String dni, String nombres, String
     * apellidos, LocalDate fechaNacimiento,
     * String genero, String correo, String telefono, LocalDateTime createdAt,
     * LocalDateTime updatedAt,
     * Integer idEstado, String codEmpleado, Cargo cargo, Sucursal sucursal) {
     * super(idPersona, dni, nombres, apellidos, fechaNacimiento, genero, correo,
     * telefono, createdAt, updatedAt, idEstado);
     * this.codEmpleado = codEmpleado;
     * this.cargo = cargo;
     * this.sucursal = sucursal;
     * }
     */
}