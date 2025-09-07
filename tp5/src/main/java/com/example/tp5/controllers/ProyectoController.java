package com.example.tp5.controllers;

import com.example.tp5.models.Proyecto;
import com.example.tp5.services.ProyectoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;


import java.util.List;

@RestController
@RequestMapping("/api/proyectos")
@Validated
public class ProyectoController {
    private final ProyectoService proyectoService;

    public ProyectoController(ProyectoService proyectoService) {
        this.proyectoService = proyectoService;
    }

    @GetMapping
    @Operation(
            summary = "Obtener todos los proyectos",
            description = "Devuelve una lista con todos los proyectos registrados"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de proyectos devuelta correctamente")
    })
    public List<Proyecto> obtenerTodos() {
        return proyectoService.obtenerTodos();
    }


    @GetMapping("/{id}")
    @Operation(
            summary = "Obtener proyecto por ID",
            description = "Devuelve un proyecto dado su ID"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "proyecto encontrado"),
            @ApiResponse(responseCode = "404", description = "No se encontró el proyecto con el ID proporcionado")
    })
    public Proyecto obtenerPorId(
            @Parameter(description = "ID del proyecto a buscar", required = true)
            @PathVariable Long id) {
        return proyectoService.buscarPorId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Crear un nuevo proyecto",
            description = "Crea un proyecto con la información proporcionada en el cuerpo de la solicitud"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "proyecto creado exitosamente")
    })
    public Proyecto crear(
            @Parameter(description = "proyecto a crear", required = true)
            @RequestBody Proyecto proyecto) {
        return proyectoService.guardar(proyecto);
    }


    @PutMapping("/{id}")
    @Operation(
            summary = "Actualizar un proyecto",
            description = "Actualiza los datos de un proyecto existente según su ID"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "proyecto actualizado correctamente"),
            @ApiResponse(responseCode = "404", description = "No se encontró el proyecto con el ID proporcionado")
    })
    public Proyecto actualizar(
            @Parameter(description = "ID del proyecto a actualizar", required = true)
            @PathVariable Long id,
            @Parameter(description = "Objeto proyecto a actualizar", required = true)
            @RequestBody Proyecto proyecto) {
        return proyectoService.actualizar(id, proyecto);
    }


    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
            summary = "Eliminar un proyecto",
            description = "Elimina un proyecto existente dado su ID"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "proyecto eliminado correctamente"),
            @ApiResponse(responseCode = "404", description = "No se encontró el proyecto con el ID proporcionado")
    })
    public void eliminar(
            @Parameter(description = "ID del proyecto a eliminar", required = true)
            @PathVariable Long id) {
        proyectoService.eliminar(id);
    }

    @GetMapping("/activos")
    @Operation(
            summary = "Obtener todos los proyectos activos",
            description = "Devuelve una lista de proyectos activos actualmente.")
    @ApiResponse(responseCode = "200", description = "Búsqueda de proyectos activos completada")
    public List<Proyecto> obtenerProyectosActivos() {
        return proyectoService.obtenerProyectosActivos();
    }
}