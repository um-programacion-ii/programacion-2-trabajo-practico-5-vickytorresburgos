package com.example.tp5.controllers;

import com.example.tp5.models.Empleado;
import com.example.tp5.services.EmpleadoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/empleados")
@Validated
public class EmpleadoController {
    private final EmpleadoService empleadoService;

    public EmpleadoController(EmpleadoService empleadoService) {
        this.empleadoService = empleadoService;
    }

    @GetMapping
    @Operation(
            summary = "Obtener todos los empleados",
            description = "Devuelve una lista con todos los empleados registrados"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de empleados devuelta correctamente")
    })
    public List<Empleado> obtenerTodos() {
        return empleadoService.obtenerTodos();
    }


    @GetMapping("/{id}")
    @Operation(
            summary = "Obtener empleado por ID",
            description = "Devuelve un empleado específico dado su ID"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Empleado encontrado"),
            @ApiResponse(responseCode = "404", description = "No se encontró el empleado con el ID proporcionado")
    })
    public Empleado obtenerPorId(
            @Parameter(description = "ID del empleado a buscar", required = true)
            @PathVariable Long id) {
        return empleadoService.buscarPorId(id);
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Crear un nuevo empleado",
            description = "Crea un empleado con la información proporcionada en el cuerpo de la solicitud"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Empleado creado exitosamente")
    })
    public Empleado crear(
            @Parameter(description = "Objeto Empleado a crear", required = true)
            @RequestBody Empleado empleado) {
        return empleadoService.guardar(empleado);
    }


    @PutMapping("/{id}")
    @Operation(
            summary = "Actualizar un empleado",
            description = "Actualiza los datos de un empleado existente según su ID"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Empleado actualizado correctamente"),
            @ApiResponse(responseCode = "404", description = "No se encontró el empleado con el ID proporcionado")
    })
    public Empleado actualizar(
            @Parameter(description = "ID del empleado a actualizar", required = true)
            @PathVariable Long id,
            @Parameter(description = "Objeto Empleado con los datos actualizados", required = true)
            @RequestBody Empleado empleado) {
        return empleadoService.actualizar(id, empleado);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
            summary = "Eliminar un empleado",
            description = "Elimina un empleado existente dado su ID"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Empleado eliminado correctamente"),
            @ApiResponse(responseCode = "404", description = "No se encontró el empleado con el ID proporcionado")
    })
    public void eliminar(
            @Parameter(description = "ID del empleado a eliminar", required = true)
            @PathVariable Long id) {
        empleadoService.eliminar(id);
    }


    @GetMapping("/departamento/{nombre}")
    @Operation(
            summary = "Obtener una lista de empleados",
            description = "Devuelve una lista de empleados que pertenecen a un departamento dado"
    )
    @ApiResponse(
            responseCode = "200", description = "Busqueda de empleados por departamento completada correctamente"
    )
    public List<Empleado> obtenerPorDepartamento(
            @Parameter(description = "nombre del departamento", required = true)
            @PathVariable String nombre) {
        return empleadoService.buscarPorDepartamento(nombre);
    }

    @Operation(
            summary = "Buscar empleados por rango de salario",
            description = "Devuelve una lista de empleados dado un rango de salario"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Búsqueda de empleado por rango salarial completada correctamente")
    @GetMapping("/salario")
    public List<Empleado> obtenerPorRangoSalario(
            @Parameter(description = "Salario minimo", required = true)
            @RequestParam Double min,
            @Parameter(description = "Salario maximo", required = true)
            @RequestParam Double max) {
        return empleadoService.buscarPorRangoSalario(min, max);
    }
}