package com.linox.sistemaventas.models;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("CLIENTE_NATURAL")
public class ClienteNatural extends Cliente {
}
