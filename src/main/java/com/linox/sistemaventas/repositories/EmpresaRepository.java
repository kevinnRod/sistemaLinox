package com.linox.sistemaventas.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.linox.sistemaventas.models.Empresa;

public interface EmpresaRepository extends JpaRepository<Empresa, Integer> {
    List<Empresa> findByIdEstado(Integer estado);
}
