package com.linox.sistemaventas.repositories;

import com.linox.sistemaventas.models.Kardex;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface KardexRepository extends JpaRepository<Kardex, Integer> {
    List<Kardex> findByIdEstado(Integer idEstado);
}
