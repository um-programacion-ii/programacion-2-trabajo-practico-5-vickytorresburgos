package com.example.tp5.services;

import com.example.tp5.exceptions.ProyectoNoEncontradoException;
import com.example.tp5.models.Proyecto;
import com.example.tp5.repositories.ProyectoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ProyectoServiceIntegrationTest {
    @Autowired
    private ProyectoService proyectoService;

    @Autowired
    private ProyectoRepository proyectoRepository;

    private Proyecto crearProyecto(String nombre, LocalDate fechaInicio, LocalDate fechaFin) {
        Proyecto proyecto = new Proyecto();
        proyecto.setNombre(nombre);
        proyecto.setDescripcion("Descripción de " + nombre);
        proyecto.setFechaInicio(fechaInicio);
        proyecto.setFechaFin(fechaFin);
        return proyecto;
    }

    @BeforeEach
    void limpiar() {
        proyectoRepository.deleteAll();
    }

    @Test
    void testGuardarProyecto() {
        Proyecto proyecto = crearProyecto("Proyecto A", LocalDate.now(), LocalDate.now().plusDays(10));

        Proyecto guardado = proyectoService.guardar(proyecto);

        assertNotNull(guardado.getId());
        assertEquals("Proyecto A", guardado.getNombre());
    }

    @Test
    void testBuscarPorId() {
        Proyecto proyecto = crearProyecto("Proyecto B", LocalDate.now(), LocalDate.now().plusDays(15));
        Proyecto guardado = proyectoRepository.save(proyecto);

        Proyecto encontrado = proyectoService.buscarPorId(guardado.getId());

        assertEquals("Proyecto B", encontrado.getNombre());
    }

    @Test
    void testBuscarPorId_NoExiste_LanzaExcepcion() {
        assertThrows(ProyectoNoEncontradoException.class, () -> proyectoService.buscarPorId(999L));
    }

    @Test
    void testObtenerTodos() {
        Proyecto proyecto1 = crearProyecto("Proyecto C", LocalDate.now(), LocalDate.now().plusDays(20));
        Proyecto proyecto2 = crearProyecto("Proyecto D", LocalDate.now(), LocalDate.now().plusDays(30));
        proyectoRepository.saveAll(List.of(proyecto1, proyecto2));

        List<Proyecto> lista = proyectoService.obtenerTodos();

        assertEquals(2, lista.size());
    }

    @Test
    void testActualizarProyecto() {
        Proyecto proyecto = crearProyecto("Proyecto E", LocalDate.now(), LocalDate.now().plusDays(25));
        Proyecto guardado = proyectoRepository.save(proyecto);

        Proyecto actualizado = crearProyecto("Proyecto E Modificado", LocalDate.now(), LocalDate.now().plusDays(40));

        Proyecto result = proyectoService.actualizar(guardado.getId(), actualizado);

        assertEquals("Proyecto E Modificado", result.getNombre());
        assertEquals(guardado.getId(), result.getId());
    }

    @Test
    void testActualizarProyecto_NoExiste_LanzaExcepcion() {
        Proyecto proyecto = crearProyecto("Proyecto F", LocalDate.now(), LocalDate.now().plusDays(10));

        assertThrows(ProyectoNoEncontradoException.class, () -> proyectoService.actualizar(999L, proyecto));
    }

    @Test
    void testEliminarProyecto() {
        Proyecto proyecto = crearProyecto("Proyecto G", LocalDate.now(), LocalDate.now().plusDays(5));
        Proyecto guardado = proyectoRepository.save(proyecto);

        proyectoService.eliminar(guardado.getId());

        assertFalse(proyectoRepository.existsById(guardado.getId()));
    }

    @Test
    void testEliminarProyecto_NoExiste_LanzaExcepcion() {
        assertThrows(ProyectoNoEncontradoException.class, () -> proyectoService.eliminar(999L));
    }

    @Test
    void testObtenerProyectosActivos() {
        Proyecto proyectoActivo = crearProyecto("Proyecto Activo", LocalDate.now().minusDays(5), LocalDate.now().plusDays(10));
        Proyecto proyectoFinalizado = crearProyecto("Proyecto Finalizado", LocalDate.now().minusDays(20), LocalDate.now().minusDays(1));
        proyectoRepository.saveAll(List.of(proyectoActivo, proyectoFinalizado));

        List<Proyecto> activos = proyectoService.obtenerProyectosActivos();

        assertEquals(1, activos.size());
        assertEquals("Proyecto Activo", activos.get(0).getNombre());
    }
}
