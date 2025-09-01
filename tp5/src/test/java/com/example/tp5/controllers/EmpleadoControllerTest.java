package com.example.tp5.controllers;


import com.example.tp5.exceptions.EmailDuplicadoException;
import com.example.tp5.exceptions.EmpleadoNoEncontradoException;
import com.example.tp5.models.Departamento;
import com.example.tp5.models.Empleado;
import com.example.tp5.services.EmpleadoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@WebMvcTest(EmpleadoController.class)
public class EmpleadoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmpleadoService empleadoService;


    @Autowired
    private ObjectMapper objectMapper;

    private Empleado empleadoTest;
    private Departamento departamentoTest;

    @BeforeEach
    void setUp() {
        departamentoTest = new Departamento();
        departamentoTest.setId(1L);
        departamentoTest.setNombre("IT");
        departamentoTest.setDescripcion("Departamento de Informática y Tecnología");

        empleadoTest = new Empleado();
        empleadoTest.setId(1L);
        empleadoTest.setApellido("Artola");
        empleadoTest.setNombre("Valentina");
        empleadoTest.setDepartamento(departamentoTest);
        empleadoTest.setEmail("valeartola@it.com");
        empleadoTest.setSalario(new BigDecimal(10000));
        empleadoTest.setFechaContratacion(LocalDate.now());
        empleadoTest.setDepartamento(departamentoTest);
        empleadoTest.setProyectos(Collections.emptySet());

    }

    @Test
    void testObtenerTodos() throws Exception {
        given(empleadoService.obtenerTodos()).willReturn(List.of(empleadoTest));

        mockMvc.perform(get("/api/empleados"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()", is(1)))
                .andExpect(jsonPath("$[0].nombre").value("Valentina"))
                .andExpect(jsonPath("$[0].departamento.nombre").value("IT"));
    }

    @Test
    void testObtenerPorId() throws Exception {
        given(empleadoService.buscarPorId(1L)).willReturn(empleadoTest);

        mockMvc.perform(get("/api/empleados/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Valentina"));
    }

    @Test
    void testObtenerPorDepartamento() throws Exception {
        given(empleadoService.buscarPorDepartamento("IT")).willReturn(List.of(empleadoTest));

        mockMvc.perform(get("/api/empleados/departamento/IT"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].nombre").value("Valentina"));
    }

    @Test
    void testObtenerPorRangoSalario() throws Exception {
        given(empleadoService.buscarPorRangoSalario(new BigDecimal(20), new BigDecimal(50000))).willReturn(List.of(empleadoTest));

        mockMvc.perform(get("/api/empleados/salario")
                        .param("min", "20")
                        .param("max", "50000"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].nombre").value("Valentina"))
                .andExpect(jsonPath("$[0].salario").value(10000));
    }

    @Test
    void testObtenerEmpleadoNoExistente() throws Exception {
        given(empleadoService.buscarPorId(10L))
                .willThrow(new EmpleadoNoEncontradoException("Empleado no encontrado"));
        mockMvc.perform(get("/api/empleados/10"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Recurso no encontrado"))
                .andExpect(jsonPath("$.message").value("Empleado no encontrado"));
    }

    @Test
    void testCrearEmpleado() throws Exception {
        given(empleadoService.guardar(empleadoTest)).willReturn(empleadoTest);

        mockMvc.perform(post("/api/empleados")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(empleadoTest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.nombre", is("Valentina")));
    }

    @Test
    void testCrearEmpleadoEmailDuplicado() throws Exception {
        given(empleadoService.guardar(any(Empleado.class)))
                .willThrow(new EmailDuplicadoException("El email ya está registrado"));
        mockMvc.perform(post("/api/empleados")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(empleadoTest)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value("Conflicto"))
                .andExpect(jsonPath("$.message").value("El email ya está registrado"));    }

    @Test
    void testActualizarEmpleado() throws Exception {
        Empleado empleadoActualizado = new Empleado(1L, "Valentina", "Artola",
                "valeartola@it.com",LocalDate.now(), new BigDecimal(20000), departamentoTest, Collections.emptySet());
        given(empleadoService.actualizar(anyLong(), any(Empleado.class))).willReturn(empleadoActualizado);

        mockMvc.perform(put("/api/empleados/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(empleadoActualizado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.salario", is(20000)));
    }

    @Test
    void testActualizarEmpleadoNoExistente() throws Exception {
        given(empleadoService.actualizar(anyLong(), any(Empleado.class)))
                .willThrow(new EmpleadoNoEncontradoException("Empleado no encontrado"));

        mockMvc.perform(put("/api/empleados/50")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(empleadoTest)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Recurso no encontrado"))
                .andExpect(jsonPath("$.message").value("Empleado no encontrado"));    }

    @Test
    void testEliminar() throws Exception {
        doNothing().when(empleadoService).eliminar(1L);

        mockMvc.perform(delete("/api/empleados/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testEliminarEmpleadoNoExistente() throws Exception {
        willThrow(new EmpleadoNoEncontradoException("Empleado no encontrado"))
                .given(empleadoService).eliminar(10L);

        mockMvc.perform(delete("/api/empleados/10"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Recurso no encontrado"))
                .andExpect(jsonPath("$.message").value("Empleado no encontrado"));    }

}
