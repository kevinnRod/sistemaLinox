package com.linox.sistemaventas.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.linox.sistemaventas.models.EmpresaAnfitrion;

@Repository
public interface EmpresaAnfitrionRepository extends JpaRepository<EmpresaAnfitrion, Integer> {
    // Puedes agregar métodos personalizados aquí si los necesitas
}
