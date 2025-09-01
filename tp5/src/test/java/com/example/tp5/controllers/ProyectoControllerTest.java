package com.example.tp5.controllers;

import com.example.tp5.exceptions.ProyectoNoEncontradoException;
import com.example.tp5.models.Proyecto;
import com.example.tp5.services.ProyectoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(ProyectoController.class)
class ProyectoControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProyectoService proyectoService;

    private Proyecto proyectoTest;

    @BeforeEach
    void setUp() {
        proyectoTest = new Proyecto(1L, "Web 3.6", "Nueva Version de Productos Web",
                LocalDate.now(), LocalDate.now().plusMonths(12), Collections.emptySet());
    }

    @Test
    void testObtenerTodos() throws Exception {
        given(proyectoService.obtenerTodos()).willReturn(List.of(proyectoTest));

        mockMvc.perform(get("/api/proyectos"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()", is(1)))
                .andExpect(jsonPath("$[0].nombre", is("Web 3.6")));
    }

    @Test
    void testObtenerPorId() throws Exception {
        given(proyectoService.buscarPorId(1L)).willReturn(proyectoTest);

        mockMvc.perform(get("/api/proyectos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Web 3.6"));
    }

    @Test
    void testObtenerPorIdNoExistente() throws Exception {
        willThrow(new ProyectoNoEncontradoException("Proyecto no encontrado"))
                .given(proyectoService).buscarPorId(10L);
        mockMvc.perform(get("/api/proyectos/10"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Recurso no encontrado"))
                .andExpect(jsonPath("$.message").value("Proyecto no encontrado"))
                .andExpect(jsonPath("$.path").value("/api/proyectos/10"));
    }

    @Test
    void testCrearProyecto() throws Exception {
        given(proyectoService.guardar(proyectoTest)).willReturn(proyectoTest);

        mockMvc.perform(post("/api/proyectos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(proyectoTest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.nombre", is("Web 3.6")));
    }

    @Test
    void testActualizarProyecto() throws Exception {
        Proyecto proyectoActualizado = new Proyecto(1L, "Web 3.6", "Nueva Version de Productos Web",
                LocalDate.now(), LocalDate.now().plusMonths(9), Collections.emptySet());

        given(proyectoService.actualizar(anyLong(), any(Proyecto.class))).willReturn(proyectoActualizado);

        mockMvc.perform(put("/api/proyectos/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(proyectoActualizado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fechaFin", is(LocalDate.now().plusMonths(9).toString())));
    }


    @Test
    void testActualizarProyectoNoExistente() throws Exception {
        Proyecto proyectoActualizado = new Proyecto(10L, "Inexistente", "No existe",
                LocalDate.now(), LocalDate.now().plusMonths(6), Collections.emptySet());

        willThrow(new ProyectoNoEncontradoException("Proyecto no encontrado"))
                .given(proyectoService).actualizar(anyLong(), any(Proyecto.class));

        mockMvc.perform(put("/api/proyectos/10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(proyectoActualizado)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Recurso no encontrado"))
                .andExpect(jsonPath("$.message").value("Proyecto no encontrado"))
                .andExpect(jsonPath("$.path").value("/api/proyectos/10"));
    }

    @Test
    void testEliminar() throws Exception {
        doNothing().when(proyectoService).eliminar(1L);

        mockMvc.perform(delete("/api/proyectos/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testEliminarProyectoNoExistente() throws Exception {
        willThrow(new ProyectoNoEncontradoException("Proyecto no encontrado"))
                .given(proyectoService).eliminar(10L);

        mockMvc.perform(delete("/api/proyectos/10"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Recurso no encontrado"))
                .andExpect(jsonPath("$.message").value("Proyecto no encontrado"))
                .andExpect(jsonPath("$.path").value("/api/proyectos/10"));
    }

    @Test
    void testObtenerProyectosActivos() throws Exception {
        given(proyectoService.obtenerProyectosActivos()).willReturn(List.of(proyectoTest));

        mockMvc.perform(get("/api/proyectos/activos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(1)))
                .andExpect(jsonPath("$[0].nombre", is(proyectoTest.getNombre())));

    }
}