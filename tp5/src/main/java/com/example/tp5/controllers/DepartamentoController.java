package com.example.tp5.controllers;

import com.example.tp5.models.Departamento;
import com.example.tp5.services.DepartamentoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/departamentos")
@Validated
public class DepartamentoController {
    private final DepartamentoService departamentoService;

    public DepartamentoController(DepartamentoService departamentoService) {
        this.departamentoService = departamentoService;
    }

    @GetMapping
    @Operation(
            summary = "obtener una lista de todos los departamentos",
            description = "devuelve una lista de todos los departamentos"
    )
    @ApiResponses(
            @ApiResponse(responseCode = "200", description = "Lista de departamentos devuelta")
    )
    public List<Departamento> obtenerTodos() {
        return departamentoService.obtenerTodos();
    }


    @GetMapping("/{id}")
    @Operation(
            summary = "Obtener departamento por ID",
            description = "Devuelve un departamento específico dado su ID"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Departamento encontrado"),
            @ApiResponse(responseCode = "404", description = "No se encontró el departamento con el ID proporcionado")
    })
    public Departamento obtenerPorId(
            @Parameter(description = "ID del departamento a buscar", required = true)
            @PathVariable Long id) {
        return departamentoService.buscarPorId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Crear un nuevo departamento",
            description = "Crea un departamento con la información proporcionada en el cuerpo de la solicitud"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Departamento creado exitosamente")
    })
    public Departamento crear(
            @Parameter(description = "Objeto Departamento a crear", required = true)
            @RequestBody Departamento departamento) {
        return departamentoService.guardar(departamento);
    }


    @PutMapping("/{id}")
    @Operation(
            summary = "Actualizar un departamento",
            description = "Actualiza los datos de un departamento existente según su ID"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Departamento actualizado correctamente"),
            @ApiResponse(responseCode = "404", description = "No se encontró el departamento con el ID proporcionado")
    })
    public Departamento actualizar(
            @Parameter(description = "ID del departamento a actualizar", required = true)
            @PathVariable Long id,
            @Parameter(description = "Objeto Departamento con los datos actualizados", required = true)
            @RequestBody Departamento departamento) {
        return departamentoService.actualizar(id, departamento);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
            summary = "Eliminar un departamento",
            description = "Elimina un departamento existente dado su ID"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Departamento eliminado correctamente"),
            @ApiResponse(responseCode = "404", description = "No se encontró el departamento con el ID proporcionado")
    })
    public void eliminar(
            @Parameter(description = "ID del departamento a eliminar", required = true)
            @PathVariable Long id) {
        departamentoService.eliminar(id);
    }
}