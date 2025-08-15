package com.example.tp5.services;

import com.example.tp5.exceptions.DepartamentoNoEncontradoException;
import com.example.tp5.models.Departamento;
import com.example.tp5.repositories.DepartamentoRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class DepartamentoServiceImpl implements DepartamentoService {
    private final DepartamentoRepository departamentoRepository;


    public DepartamentoServiceImpl(DepartamentoRepository departamentoRepository) {
        this.departamentoRepository = departamentoRepository;
    }

    @Override
    public Departamento guardar(Departamento departamento) {
        if (departamento.getNombre() == null) {
            throw new IllegalArgumentException("El nombre del departamento no puede estar vacío");
        }
        return departamentoRepository.save(departamento);
    }

    @Override
    public Departamento buscarPorId(Long id) {
        return departamentoRepository.findById(id)
                .orElseThrow(() -> new DepartamentoNoEncontradoException(
                        "No se encontró el departamento con el ID: " + id
                ));
    }

    @Override
    public List<Departamento> obtenerTodos() {
        return departamentoRepository.findAll();
    }

    @Override
    public Departamento actualizar(Long id, Departamento departamento) {
        return departamentoRepository.findById(id)
                .map(departamentoExistente -> {
                    departamentoExistente.setNombre(departamento.getNombre());
                    departamentoExistente.setDescripcion(departamento.getDescripcion());
                    return departamentoRepository.save(departamentoExistente);
                })
                .orElseThrow(() -> new DepartamentoNoEncontradoException(
                        "No se encontró el departamento con el ID: " + id
                ));
    }

    @Override
    public void eliminar(Long id) {
        Departamento departamento = departamentoRepository.findById(id)
                .orElseThrow(() -> new DepartamentoNoEncontradoException(
                        "No se encontró el departamento con el ID: " + id
                ));
        departamentoRepository.delete(departamento);
    }

}
