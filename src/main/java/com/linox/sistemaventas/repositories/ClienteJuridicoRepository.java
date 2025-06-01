package com.linox.sistemaventas.repositories;

import com.linox.sistemaventas.models.ClienteJuridico;
import com.linox.sistemaventas.models.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteJuridicoRepository extends JpaRepository<ClienteJuridico, String> {
    boolean existsByEmpresa(Empresa empresa);
}
