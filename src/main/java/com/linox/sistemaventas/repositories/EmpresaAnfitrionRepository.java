package com.linox.sistemaventas.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.linox.sistemaventas.models.EmpresaAnfitrion;

@Repository
public interface EmpresaAnfitrionRepository extends JpaRepository<EmpresaAnfitrion, Integer> {
    Optional<EmpresaAnfitrion> findFirstByOrderByIdEmpresaAsc();
}
