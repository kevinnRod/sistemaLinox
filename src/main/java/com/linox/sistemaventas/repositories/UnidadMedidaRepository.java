package com.linox.sistemaventas.repositories;

import com.linox.sistemaventas.models.UnidadMedida;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UnidadMedidaRepository extends JpaRepository<UnidadMedida, Integer> {
    List<UnidadMedida> findByIdEstado(Integer estado);
}
