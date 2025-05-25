package com.linox.sistemaventas.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.linox.sistemaventas.models.Empresa;

public interface EmpresaRepository extends JpaRepository<Empresa, Integer> {
    List<Empresa> findByIdEstado(Integer estado);
    Optional<Empresa> findByRuc(String ruc);
    Optional<Empresa> findByRazonSocial(String razonSocial);

}
