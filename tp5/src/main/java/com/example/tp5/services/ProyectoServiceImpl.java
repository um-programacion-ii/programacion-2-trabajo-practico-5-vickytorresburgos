package com.example.tp5.services;


import com.example.tp5.exceptions.EmpleadoNoEncontradoException;
import com.example.tp5.exceptions.ProyectoNoEncontradoException;
import com.example.tp5.models.Empleado;
import com.example.tp5.models.Proyecto;
import com.example.tp5.repositories.ProyectoRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class ProyectoServiceImpl implements ProyectoService {
    private final ProyectoRepository proyectoRepository;

    public ProyectoServiceImpl(ProyectoRepository proyectoRepository) {
        this.proyectoRepository = proyectoRepository;
    }

    @Override
    public Proyecto guardar(Proyecto proyecto) {
        return proyectoRepository.save(proyecto);
    }

    @Override
    public Proyecto buscarPorId(Long id) {
        return proyectoRepository.findById(id)
                .orElseThrow(() -> new ProyectoNoEncontradoException("No se encontró el proyecto con ID: " + id));
    }

    @Override
    public List<Proyecto> obtenerTodos() {
        return proyectoRepository.findAll();
    }

    @Override
    public Proyecto actualizar(Long id, Proyecto proyecto) {
        if (!proyectoRepository.existsById(id)) {
            throw new ProyectoNoEncontradoException("No se encontró el proyecto con ID: " + id);
        }
        proyecto.setId(id);
        return proyectoRepository.save(proyecto);
    }

    @Override
    public void eliminar(Long id) {
        if (!proyectoRepository.existsById(id)) {
            throw new ProyectoNoEncontradoException("No se encontró el proyecto con ID: " + id);
        }
        proyectoRepository.deleteById(id);
    }

    @Override
    public List<Proyecto> obtenerProyectosActivos() {
        return proyectoRepository.findProyectosActivos(LocalDate.now());
    }
}
