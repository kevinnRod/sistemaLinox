package com.linox.sistemaventas.services.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.linox.sistemaventas.models.Kardex;
import com.linox.sistemaventas.repositories.KardexRepository;
import com.linox.sistemaventas.services.KardexService;
import org.springframework.data.domain.Pageable;
import java.util.Collections;



@Service
public class KardexServiceImpl implements KardexService {

    @Autowired
    private KardexRepository kardexRepository;

    @Override
    public List<Kardex> findAll() {
        return kardexRepository.findAll();
    }

    @Override
    public List<Kardex> findAllActivos() {
        return kardexRepository.findByIdEstadoOrderByFechaMovimientoDesc(1); // Solo activos
    }

    @Override
    public Optional<Kardex> findById(Integer id) {
        return kardexRepository.findById(id);
    }

    @Override
    public Kardex save(Kardex kardex) {
        return kardexRepository.save(kardex);
    }

    @Override
    public void deleteLogico(Integer id) {
        kardexRepository.findById(id).ifPresent(k -> {
            k.setIdEstado(2); // Inactivo
            kardexRepository.save(k);
        });
    }

    @Override
    public Page<Kardex> buscarKardex(String producto, String tipo, LocalDate fechaInicio, LocalDate fechaFin, int page, int size) {
        List<Kardex> lista = findAllActivos();

        if (producto != null && !producto.isBlank()) {
            lista = lista.stream()
                .filter(k -> k.getProducto().getNombreProducto().toLowerCase().contains(producto.toLowerCase()))
                .collect(Collectors.toList());
        }
        if (tipo != null && !tipo.isBlank()) {
            lista = lista.stream()
                .filter(k -> k.getTipoMovimiento().getNombre().equalsIgnoreCase(tipo))
                .collect(Collectors.toList());
        }
        if (fechaInicio != null) {
            lista = lista.stream()
                .filter(k -> !k.getFechaMovimiento().toLocalDate().isBefore(fechaInicio))
                .collect(Collectors.toList());
        }
        if (fechaFin != null) {
            lista = lista.stream()
                .filter(k -> !k.getFechaMovimiento().toLocalDate().isAfter(fechaFin))
                .collect(Collectors.toList());
        }

        int total = lista.size();
        int fromIndex = page * size;
        int toIndex = Math.min(fromIndex + size, total);

        List<Kardex> pageContent = fromIndex > total ? Collections.emptyList() : lista.subList(fromIndex, toIndex);
        return new PageImpl<>(pageContent, PageRequest.of(page, size), total);
    }

}
