package com.example.tp5.services;

import com.example.tp5.exceptions.DepartamentoNoEncontradoException;
import com.example.tp5.models.Departamento;

import com.example.tp5.repositories.DepartamentoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class DepartamentoServiceIntegrationTest {
    @Autowired
    private DepartamentoService departamentoService;

    @Autowired
    private DepartamentoRepository departamentoRepository;

    @Test
    void testGuardarDepartamento() {
        Departamento departamento = new Departamento();
        departamento.setNombre("RRHH");
        departamento.setDescripcion("Recursos Humanos");

        Departamento guardado = departamentoService.guardar(departamento);

        assertNotNull(guardado.getId());
        assertEquals("RRHH", guardado.getNombre());
    }

    @Test
    void testGuardarDepartamentoSinNombre() {
        Departamento dpto = new Departamento();
        dpto.setDescripcion("Sin nombre");

        assertThrows(IllegalArgumentException.class, () -> departamentoService.guardar(dpto));
    }

    @Test
    void testBuscarPorId() {
        Departamento departamento = new Departamento();
        departamento.setNombre("IT");
        departamento.setDescripcion("Tecnología");
        Departamento guardado = departamentoRepository.save(departamento);

        Departamento encontrado = departamentoService.buscarPorId(guardado.getId());

        assertEquals("IT", encontrado.getNombre());
    }

    @Test
    void testBuscarPorId_DepartamentoNoExiste() {
        assertThrows(DepartamentoNoEncontradoException.class, () -> departamentoService.buscarPorId(999L));
    }

    @Test
    void testActualizar() {
        Departamento departamento = new Departamento();
        departamento.setNombre("Ventas");
        departamento.setDescripcion("Departamento de Ventas");
        Departamento guardado = departamentoRepository.save(departamento);

        Departamento cambios = new Departamento();
        cambios.setNombre("Comercial");
        cambios.setDescripcion("Área Comercial");

        Departamento actualizado = departamentoService.actualizar(guardado.getId(), cambios);

        assertEquals("Comercial", actualizado.getNombre());
        assertEquals("Área Comercial", actualizado.getDescripcion());
    }

    @Test
    void testActualizar_DepartamentoNoExiste() {
        Departamento cambios = new Departamento();
        cambios.setNombre("Inexistente");
        cambios.setDescripcion("No existe");

        assertThrows(DepartamentoNoEncontradoException.class,
                () -> departamentoService.actualizar(123L, cambios));
    }

    @Test
    void testEliminar() {
        Departamento departamento = new Departamento();
        departamento.setNombre("Legal");
        departamento.setDescripcion("Asuntos Legales");
        Departamento guardado = departamentoRepository.save(departamento);

        departamentoService.eliminar(guardado.getId());

        assertFalse(departamentoRepository.findById(guardado.getId()).isPresent());
    }

    @Test
    void testEliminar_DepartamentoNoExiste() {
        assertThrows(DepartamentoNoEncontradoException.class, () -> departamentoService.eliminar(321L));
    }

    @Test
    void testObtenerTodos() {
        Departamento departamento1 = new Departamento();
        departamento1.setNombre("Compras");
        Departamento departamento2 = new Departamento();
        departamento2.setNombre("Logística");
        departamentoRepository.save(departamento1);
        departamentoRepository.save(departamento2);

        List<Departamento> lista = departamentoService.obtenerTodos();

        assertEquals(2, lista.size());
    }
}
