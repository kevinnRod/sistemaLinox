package com.linox.sistemaventas.services.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.linox.sistemaventas.models.Rol;
import com.linox.sistemaventas.models.Usuario;
import com.linox.sistemaventas.models.UsuarioRol;
import com.linox.sistemaventas.models.UsuarioRolId;
import com.linox.sistemaventas.repositories.UsuarioRolRepository;
import com.linox.sistemaventas.services.UsuarioRolService;

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

    @Override
    public List<UsuarioRol> findAllByEstadoActivo(Integer idUsuario) {
        return usuarioRolRepository.findByUsuarioIdUsuarioAndIdEstado(idUsuario, 1);
    }

    @Override
    public boolean existsByUsuarioAndRol(Usuario usuario, Rol rol) {
        return usuarioRolRepository.existsByUsuarioIdUsuarioAndRolIdRol(usuario.getIdUsuario(), rol.getIdRol());
    }

    @Override
    public Optional<UsuarioRol> findByUsuarioAndRol(Usuario usuario, Rol rol) {
        return usuarioRolRepository.findByUsuarioIdUsuarioAndRolIdRol(usuario.getIdUsuario(), rol.getIdRol());
    }
}