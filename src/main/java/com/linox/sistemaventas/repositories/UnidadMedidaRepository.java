package com.linox.sistemaventas.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.linox.sistemaventas.models.UnidadMedida;

public interface UnidadMedidaRepository extends JpaRepository<UnidadMedida, Integer> {
    List<UnidadMedida> findByIdEstado(Integer estado);

    boolean existsByDescripcion(String descripcion);

    boolean existsBySimbolo(String simbolo);

}
