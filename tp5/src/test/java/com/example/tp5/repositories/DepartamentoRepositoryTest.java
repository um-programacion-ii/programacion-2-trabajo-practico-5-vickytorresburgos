package com.example.tp5.repositories;

import com.example.tp5.models.Departamento;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("h2")
public class DepartamentoRepositoryTest {

    @Autowired
    private DepartamentoRepository departamentoRepository;


    @Test
    void testGuardarDepto() {
        Departamento departamento = new Departamento();
        departamento.setNombre("RRHH");
        departamento.setDescripcion("Departamento de Recursos Humanos");

        Departamento departamentoGuardado = departamentoRepository.save(departamento);

        assertEquals("RRHH", departamentoGuardado.getNombre());
        assertEquals("Departamento de Recursos Humanos", departamentoGuardado.getDescripcion());
        assertNotNull(departamentoGuardado.getId());
    }

    @Test
    void testBuscarDeptoPorId() {
        Departamento departamento = new Departamento();
        departamento.setNombre("RRHH");
        Departamento departamentoGuardado = departamentoRepository.save(departamento);

        Optional<Departamento> departamentoEncontrado = departamentoRepository.findById(departamentoGuardado.getId());

        assertTrue(departamentoEncontrado.isPresent());
        assertEquals("RRHH", departamentoEncontrado.get().getNombre());
    }

    @Test
    void testObtenerTodos() {
        Departamento departamento1 = new Departamento();
        departamento1.setNombre("RRHH");
        departamentoRepository.save(departamento1);

        Departamento departamento2 = new Departamento();
        departamento2.setNombre("IT");
        departamentoRepository.save(departamento2);

        List<Departamento> departamentos = departamentoRepository.findAll();

        assertEquals(2, departamentos.size());
    }

    @Test
    void testActualizar() {
        Departamento departamento = new Departamento();
        departamento.setNombre("Recursos Humanos");
        Departamento departamentoGuardado = departamentoRepository.save(departamento);

        departamentoGuardado.setNombre("RRHH");
        Departamento departamentoActualizado = departamentoRepository.save(departamentoGuardado);

        assertEquals("RRHH", departamentoActualizado.getNombre());
    }

    @Test
    void testEliminar() {
        Departamento departamento = new Departamento();
        departamento.setNombre("Recursos Humanos");
        Departamento departamentoGuardado = departamentoRepository.save(departamento);

        departamentoRepository.delete(departamentoGuardado);

        Optional<Departamento> departamentos = departamentoRepository.findById(departamentoGuardado.getId());
        assertTrue(departamentos.isEmpty());
    }

}
