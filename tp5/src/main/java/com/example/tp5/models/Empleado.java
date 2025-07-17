package com.example.tp5.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class Empleado {
    private Long id;
    private String nombre;
    private String apellido;
    private String email;
    private LocalDate fechaContratacion;
    private BigDecimal salario;
}