package com.linox.sistemaventas.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.linox.sistemaventas.models.EmpresaAnfitrion;
import java.util.Optional;

@Repository
public interface EmpresaAnfitrionRepository extends JpaRepository<EmpresaAnfitrion, Integer> {
    Optional<EmpresaAnfitrion> findFirstByOrderByIdEmpresaAsc();
}
