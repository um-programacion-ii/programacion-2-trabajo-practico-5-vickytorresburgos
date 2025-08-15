package com.example.tp5.services;

import com.example.tp5.models.Departamento;
import com.example.tp5.models.Empleado;

import java.math.BigDecimal;
import java.util.List;

public interface DepartamentoService {
    Departamento guardar(Departamento departamento);
    Departamento buscarPorId(Long id);
    List<Departamento> obtenerTodos();
    Departamento actualizar(Long id, Departamento departamento);
    void eliminar(Long id);
}
