package com.linox.sistemaventas.services.impl;

import com.linox.sistemaventas.models.Cargo;
import com.linox.sistemaventas.repositories.CargoRepository;
import com.linox.sistemaventas.services.CargoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CargoServiceImpl implements CargoService {

    @Autowired
    private CargoRepository cargoRepository;

    @Override
    public List<Cargo> listar() {
        return cargoRepository.findAll();
    }

    @Override
    public Optional<Cargo> obtenerPorId(Integer id) {
        return cargoRepository.findById(id);
    }

    @Override
    public Cargo guardar(Cargo cargo) {
        return cargoRepository.save(cargo);
    }

    @Override
    public void eliminar(Integer id) {
        cargoRepository.deleteById(id);
    }

    @Override
    public boolean existePorNombre(String nombreCargo) {
        return cargoRepository.existsByNombreCargo(nombreCargo);
    }
}
