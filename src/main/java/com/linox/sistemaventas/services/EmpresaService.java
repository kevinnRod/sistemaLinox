package com.linox.sistemaventas.services;

import java.util.List;
import java.util.Optional;

import com.linox.sistemaventas.models.Empresa;

public interface EmpresaService {
    List<Empresa> findAll(); // <- Añadir este método

    Empresa guardar(Empresa empresa);

    Empresa actualizar(Empresa empresa);

    List<Empresa> listarActivas();

    Empresa buscarPorId(Integer id);

    Optional<Empresa> buscarPorRuc(String ruc);
    Optional<Empresa> buscarPorRazonSocial(String razonSocial);

}
