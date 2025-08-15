package com.example.tp5.services;

import com.example.tp5.models.Empleado;
import com.example.tp5.models.Proyecto;
import java.util.List;

public interface ProyectoService {
    Proyecto guardar(Proyecto proyecto);
    Proyecto buscarPorId(Long id);
    List<Proyecto> obtenerTodos();
    Proyecto actualizar(Long id, Proyecto proyecto);
    void eliminar(Long id);
    List<Proyecto> obtenerProyectosActivos();
}
