package com.linox.sistemaventas.services.impl;

import com.linox.sistemaventas.entities.UsuarioRol;
import com.linox.sistemaventas.entities.UsuarioRolId;
import com.linox.sistemaventas.repositories.UsuarioRolRepository;
import com.linox.sistemaventas.services.UsuarioRolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioRolServiceImpl implements UsuarioRolService {

    @Autowired
    private UsuarioRolRepository usuarioRolRepository;

    @Override
    public List<UsuarioRol> findAll() {
        return usuarioRolRepository.findAll();
    }

    @Override
    public Optional<UsuarioRol> findById(UsuarioRolId id) {
        return usuarioRolRepository.findById(id);
    }

    @Override
    public UsuarioRol save(UsuarioRol usuarioRol) {
        return usuarioRolRepository.save(usuarioRol);
    }

    @Override
    public void deleteById(UsuarioRolId id) {
        usuarioRolRepository.deleteById(id);
    }

    @Override
    public List<UsuarioRol> findByUsuarioId(Integer idUsuario) {
        return usuarioRolRepository.findByUsuarioIdUsuario(idUsuario);
    }
}