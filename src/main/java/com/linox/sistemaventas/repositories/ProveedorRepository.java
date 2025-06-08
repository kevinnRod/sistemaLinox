package com.linox.sistemaventas.repositories;

import com.linox.sistemaventas.models.Empresa;
import com.linox.sistemaventas.models.Proveedor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProveedorRepository extends JpaRepository<Proveedor, Integer> {
    List<Proveedor> findByIdEstado(Integer estado);

    boolean existsByEmpresa(Empresa empresa);
}
