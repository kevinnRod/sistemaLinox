package com.linox.sistemaventas.services.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.linox.sistemaventas.models.EmpresaAnfitrion;
import com.linox.sistemaventas.repositories.EmpresaAnfitrionRepository;
import com.linox.sistemaventas.services.EmpresaAnfitrionService;

@Service
public class EmpresaAnfitrionServiceImpl implements EmpresaAnfitrionService {

    @Autowired
    private EmpresaAnfitrionRepository empresaAnfitrionRepository;

    @Override
    public List<EmpresaAnfitrion> findAll() {
        return empresaAnfitrionRepository.findAll();
    }

    @Override
    public Optional<EmpresaAnfitrion> findById(Integer id) {
        return empresaAnfitrionRepository.findById(id);
    }

    @Override
    public EmpresaAnfitrion save(EmpresaAnfitrion empresaAnfitrion) {
        return empresaAnfitrionRepository.save(empresaAnfitrion);
    }

    @Override
    public void deleteById(Integer id) {
        empresaAnfitrionRepository.deleteById(id);
    }
}
