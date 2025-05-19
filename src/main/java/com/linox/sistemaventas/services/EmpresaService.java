package com.linox.sistemaventas.services;

import java.util.List;

import com.linox.sistemaventas.models.Empresa;

public interface EmpresaService {
    Empresa guardar(Empresa empresa);

    Empresa actualizar(Empresa empresa);

    List<Empresa> listarActivas();

    Empresa buscarPorId(Integer id);
}
