package com.example.tp5.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Departamento {
    private Long id;
    private String nombre;
    private String descripcion;
    private List<Empleado> empleados = new ArrayList<>();
}
