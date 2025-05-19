package com.linox.sistemaventas.services;

import java.util.List;
import java.util.Optional;
import com.linox.sistemaventas.models.EmpresaAnfitrion;

public interface EmpresaAnfitrionService {
    List<EmpresaAnfitrion> findAll();

    Optional<EmpresaAnfitrion> findById(Integer id);

    EmpresaAnfitrion save(EmpresaAnfitrion empresaAnfitrion);

    void deleteById(Integer id);
}
