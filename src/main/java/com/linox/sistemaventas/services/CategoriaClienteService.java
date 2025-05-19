package com.linox.sistemaventas.services;

import com.linox.sistemaventas.models.CategoriaCliente;
import java.util.List;
import java.util.Optional;

public interface CategoriaClienteService {
    List<CategoriaCliente> findAll();

    List<CategoriaCliente> findAllActivos();

    Optional<CategoriaCliente> findById(Integer id);

    CategoriaCliente save(CategoriaCliente categoriaCliente);

    void deleteById(Integer id); // puede ser para eliminación lógica o física
}
