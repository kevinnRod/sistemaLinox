package com.linox.sistemaventas.services.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.linox.sistemaventas.models.Cargo;
import com.linox.sistemaventas.repositories.CargoRepository;
import com.linox.sistemaventas.services.CargoService;

@Service
public class CargoServiceImpl implements CargoService {

    @Autowired
    private CargoRepository cargoRepository;

    @Override
    public List<Cargo> listar() {
        return cargoRepository.findAll();
    }

    @Override
    public List<Cargo> findAllActivos() {
        return cargoRepository.findByIdEstado(1); // Método personalizado en el repository
    }

    @Override
    public Optional<Cargo> findById(Integer id) {
        return cargoRepository.findById(id);
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

    @Override
    public Optional<Cargo> buscarPorNombre(String nombreCargo) {
        return cargoRepository.findByNombreCargoIgnoreCase(nombreCargo);
    }

}
