package com.example.tp5.services;


import com.example.tp5.exceptions.EmailDuplicadoException;
import com.example.tp5.exceptions.EmpleadoNoEncontradoException;
import com.example.tp5.models.Departamento;
import com.example.tp5.models.Empleado;
import com.example.tp5.repositories.DepartamentoRepository;
import com.example.tp5.repositories.EmpleadoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;


import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)

public class EmpleadoServiceIntegrationTest {
    @Autowired
    private EmpleadoService empleadoService;

    @Autowired
    private EmpleadoRepository empleadoRepository;

    @Autowired
    private DepartamentoRepository departamentoRepository;

    @BeforeEach
    void limpiar() {
        empleadoRepository.deleteAll();
    }

    private Empleado crearEmpleado(String nombre, String apellido, String email, Double salario, Departamento departamento) {
        Empleado empleado = new Empleado();
        empleado.setNombre(nombre);
        empleado.setApellido(apellido);
        empleado.setEmail(email);
        empleado.setSalario(salario);
        empleado.setFechaContratacion(LocalDate.now());
        empleado.setDepartamento(departamento);
        return empleado;
    }

    @Test
    void testGuardarEmpleado() {
        Departamento departamento = new Departamento();
        departamento.setNombre("IT");
        departamentoRepository.save(departamento);

        Empleado empleado = crearEmpleado("Ana", "Lopez", "ana@mail.com", 2000.0, departamento);

        Empleado guardado = empleadoService.guardar(empleado);

        assertNotNull(guardado.getId());
        assertEquals("Ana", guardado.getNombre());
    }

    @Test
    void testGuardarEmailDuplicado() {
        Departamento departamento = new Departamento();
        departamento.setNombre("RRHH");
        departamentoRepository.save(departamento);

        Empleado empleado1 = crearEmpleado("Juan", "Perez", "juan@mail.com", 4000.0, departamento);
        empleadoService.guardar(empleado1);

        Empleado empleado2 = crearEmpleado("Maria", "Gomez", "juan@mail.com", 4500.0, departamento);

        assertThrows(EmailDuplicadoException.class, () -> empleadoService.guardar(empleado2));
    }

    @Test
    void testBuscarPorId() {
        Departamento departamento = new Departamento();
        departamento.setNombre("RRHH");
        departamentoRepository.save(departamento);

        Empleado empleado = crearEmpleado("Ana", "Lopez", "ana@mail.com", 2000.0, departamento);
        Empleado guardado = empleadoRepository.save(empleado);

        Empleado encontrado = empleadoService.buscarPorId(guardado.getId());

        assertEquals("Ana", encontrado.getNombre());
    }

    @Test
    void testBuscarPorId_NoExiste_LanzaExcepcion() {
        assertThrows(EmpleadoNoEncontradoException.class, () -> empleadoService.buscarPorId(999L));
    }

    @Test
    void testBuscarPorDepartamento() {
        Departamento departamento = new Departamento();
        departamento.setNombre("Ventas");
        departamentoRepository.save(departamento);

        Empleado empleado1 = crearEmpleado("Juan", "Perez", "juan@mail.com", 4000.0, departamento);
        Empleado empleado2 = crearEmpleado("Maria", "Gomez", "maria@mail.com", 4500.0, departamento);
        empleadoRepository.save(empleado1);
        empleadoRepository.save(empleado2);

        List<Empleado> lista = empleadoService.buscarPorDepartamento("Ventas");

        assertEquals(2, lista.size());
    }

    @Test
    void testBuscarPorRangoSalario() {
        Departamento departamento = new Departamento();
        departamento.setNombre("Ventas");
        departamentoRepository.save(departamento);

        Empleado empleado1 = crearEmpleado("Juan", "Perez", "juan@mail.com", 4000.0, departamento);
        Empleado empleado2 = crearEmpleado("Maria", "Gomez", "maria@mail.com", 4500.0, departamento);
        empleadoRepository.save(empleado1);
        empleadoRepository.save(empleado2);

        List<Empleado> lista = empleadoService.buscarPorRangoSalario(2000.0, 5000.0);

        assertEquals(2, lista.size());
        assertTrue(lista.stream().anyMatch(e -> e.getNombre().equals("Juan")));
        assertTrue(lista.stream().anyMatch(e -> e.getNombre().equals("Maria")));
    }
/// // hasta aca funciona
    @Test
    void testObtenerSalarioPromedioPorDepartamento() {
        Departamento departamento = new Departamento();
        departamento.setNombre("Logística");
        departamentoRepository.save(departamento);

        Empleado empleado1 = crearEmpleado("Juan", "Perez", "juan@mail.com", 4000.0, departamento);
        Empleado empleado2 = crearEmpleado("Maria", "Gomez", "maria@mail.com", 4500.0, departamento);
        empleadoRepository.save(empleado1);
        empleadoRepository.save(empleado2);

        Double promedio = empleadoService.obtenerSalarioPromedioPorDepartamento(departamento.getId());

        assertEquals(4250.0, promedio);
    }

    @Test
    void testActualizarEmpleado() {
        Departamento departamento = new Departamento();
        departamento.setNombre("Logística");
        departamentoRepository.save(departamento);

        Empleado empleado = crearEmpleado("Sofia", "Perez", "sofia@mail.com", 4000.0, departamento);
        Empleado guardado = empleadoRepository.save(empleado);

        Empleado cambios = crearEmpleado("Sofia", "Perez", "sofia_nuevo@mail.com", 4000.0, departamento);

        Empleado actualizado = empleadoService.actualizar(guardado.getId(), cambios);

        assertEquals("sofia_nuevo@mail.com", actualizado.getEmail());
        assertEquals(4000.0, actualizado.getSalario());
    }

    @Test
    void testActualizarEmpleadoNoExiste() {
        Departamento departamento = new Departamento();
        departamento.setNombre("Logística");
        departamentoRepository.save(departamento);

        Empleado cambios = crearEmpleado("Pedro", "Gomez", "pedro@mail.com", 4000.0, departamento);

        assertThrows(EmpleadoNoEncontradoException.class,
                () -> empleadoService.actualizar(123L, cambios));
    }

    @Test
    void testEliminarEmpleado() {
        Departamento departamento = new Departamento();
        departamento.setNombre("Logística");
        departamentoRepository.save(departamento);

        Empleado empleado = crearEmpleado("Clara", "Dominguez", "clara@mail.com", 4000.0, departamento);
        Empleado guardado = empleadoRepository.save(empleado);

        empleadoService.eliminar(guardado.getId());

        assertFalse(empleadoRepository.findById(guardado.getId()).isPresent());
    }

    @Test
    void testEliminar_EmpleadoNoExiste_LanzaExcepcion() {
        assertThrows(EmpleadoNoEncontradoException.class, () -> empleadoService.eliminar(555L));
    }

}
