package com.linox.sistemaventas.repositories;

import com.linox.sistemaventas.models.CategoriaCliente;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CategoriaClienteRepository extends JpaRepository<CategoriaCliente, Integer> {
    List<CategoriaCliente> findByIdEstado(Integer estado);
}
