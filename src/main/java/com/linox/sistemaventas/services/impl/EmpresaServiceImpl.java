package com.linox.sistemaventas.services.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.linox.sistemaventas.models.Empresa;
import com.linox.sistemaventas.repositories.EmpresaRepository;
import com.linox.sistemaventas.services.EmpresaService;

@Service
public class EmpresaServiceImpl implements EmpresaService {

    @Autowired
    private EmpresaRepository empresaRepository;

    @Override
    public Empresa guardar(Empresa empresa) {
        return empresaRepository.save(empresa);
    }

    @Override
    public Empresa actualizar(Empresa empresa) {
        if (empresa.getIdEmpresa() != null && empresaRepository.existsById(empresa.getIdEmpresa())) {
            return empresaRepository.save(empresa);
        }
        return null; // O lanzar excepción personalizada
    }

    @Override
    public List<Empresa> listarActivas() {
        return empresaRepository.findByIdEstado(1);
    }

    @Override
    public Empresa buscarPorId(Integer id) {
        Optional<Empresa> opt = empresaRepository.findById(id);
        return opt.orElse(null);
    }
}
