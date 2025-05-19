package com.linox.sistemaventas.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.linox.sistemaventas.models.Cliente;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Integer> {
    Cliente findByCodigoCliente(String codigoCliente);

    List<Cliente> findByIdEstado(Integer idEstado);

}
