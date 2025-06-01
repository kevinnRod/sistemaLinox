package com.linox.sistemaventas.repositories;

import com.linox.sistemaventas.models.ClienteNatural;
import com.linox.sistemaventas.models.Persona;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteNaturalRepository extends JpaRepository<ClienteNatural, String> {
    boolean existsByPersona(Persona persona);
}
