package com.example.tp5.repositories;

import com.example.tp5.models.Departamento;
import com.example.tp5.models.Empleado;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("h2")

public class EmpleadoRepositoryTest {
    @Autowired
    private EmpleadoRepository empleadoRepository;

    @Autowired
    private DepartamentoRepository departamentoRepository;

    @Test
    void testFindByEmail() {
        Empleado empleado = new Empleado();
        empleado.setNombre("Tadeo");
        empleado.setApellido("Drube");
        empleado.setEmail("tadeo@email.com");
        empleado.setFechaContratacion(LocalDate.now());
        empleado.setSalario(new BigDecimal(5000000));
        empleadoRepository.save(empleado);

        Optional<Empleado> empleadosPorMail = empleadoRepository.findByEmail("tadeo@email.com");

        assertTrue(empleadosPorMail.isPresent());
        assertEquals("tadeo@email.com", empleadosPorMail.get().getEmail());

    }

    @Test
    void testFindByDepto() {
        Departamento departamento = new Departamento();
        departamento.setNombre("IT");
        departamento.setDescripcion("Departamento de Tecnología");
        departamentoRepository.save(departamento);

        Empleado empleado = new Empleado();
        empleado.setNombre("Tadeo");
        empleado.setApellido("Drube");
        empleado.setEmail("tadeo@email.com");
        empleado.setFechaContratacion(LocalDate.now());
        empleado.setSalario(new BigDecimal(5000000));
        empleado.setDepartamento(departamento);
        empleadoRepository.save(empleado);

        List<Empleado> empleadosPorDepto = empleadoRepository.findByDepartamento(departamento);

        assertEquals(1, empleadosPorDepto.size());
    }

    @Test
    void testFindBySalarioBetween() {
        Empleado empleado = new Empleado();
        empleado.setNombre("Tadeo");
        empleado.setApellido("Drube");
        empleado.setEmail("tadeo@email.com");
        empleado.setFechaContratacion(LocalDate.now());
        empleado.setSalario(new BigDecimal(5000000));
        empleadoRepository.save(empleado);

        List<Empleado> empleadosPorRangoSalario = empleadoRepository.findBySalarioBetween(new BigDecimal(200), new BigDecimal(6000000));

        assertEquals(1, empleadosPorRangoSalario.size());
        assertEquals("Tadeo", empleadosPorRangoSalario.get(0).getNombre());
        assertEquals(new BigDecimal(5000000), empleadosPorRangoSalario.get(0).getSalario());
    }

    @Test
    void testFindByFechaContratacionAfter() {
        Empleado empleado1 = new Empleado();
        empleado1.setNombre("Victoria");
        empleado1.setApellido("Torres");
        empleado1.setEmail("vicky@email.com");
        empleado1.setFechaContratacion(LocalDate.now().minusDays(5));
        empleado1.setSalario(new BigDecimal(6000000));
        empleadoRepository.save(empleado1);

        Empleado empleado2 = new Empleado();
        empleado2.setNombre("Tadeo");
        empleado2.setApellido("Drube");
        empleado2.setEmail("tadeo@email.com");
        empleado2.setFechaContratacion(LocalDate.now().minusDays(10));
        empleado2.setSalario(new BigDecimal(5000000));
        empleadoRepository.save(empleado2);

        List<Empleado> empleadosPorFechaContratacion = empleadoRepository.findByFechaContratacionAfter(LocalDate.now().minusDays(8));

        assertEquals(1, empleadosPorFechaContratacion.size());
        assertEquals("Victoria", empleadosPorFechaContratacion.get(0).getNombre());
    }

    @Test
    void testBuscarPorNombreDepto() {
        Departamento departamento = new Departamento();
        departamento.setNombre("RRHH");
        departamento.setDescripcion("Departamento de Recursos Humanos");
        departamentoRepository.save(departamento);

        Empleado empleado = new Empleado();
        empleado.setNombre("Victoria");
        empleado.setApellido("Torres");
        empleado.setEmail("vicky@email.com");
        empleado.setSalario(new BigDecimal(6000000));
        empleadoRepository.save(empleado);

        List<Empleado> empleados = empleadoRepository.findByNombreDepartamento("RRHH");

        assertEquals(1, empleados.size());
        assertEquals("Victoria", empleados.get(0).getNombre());
    }

    @Test
    void testCalcularSalarioPromedioPorDepartamento() {
        Departamento departamento = new Departamento();
        departamento.setNombre("RRHH");
        departamento.setDescripcion("Departamento de Recursos Humanos");
        departamentoRepository.save(departamento);

        Empleado empleado1 = new Empleado();
        empleado1.setNombre("Victoria");
        empleado1.setApellido("Torres");
        empleado1.setEmail("vicky@email.com");
        empleado1.setFechaContratacion(LocalDate.now());
        empleado1.setSalario(new BigDecimal(6000000));
        empleado1.setDepartamento(departamento);
        empleadoRepository.save(empleado1);

        Empleado empleado2 = new Empleado();
        empleado2.setNombre("Tadeo");
        empleado2.setApellido("Drube");
        empleado2.setEmail("tadeo@email.com");
        empleado2.setFechaContratacion(LocalDate.now());
        empleado2.setSalario(new BigDecimal(5000000));
        empleado2.setDepartamento(departamento);
        empleadoRepository.save(empleado2);

        Optional<Double> salarioPromedio = empleadoRepository.findAverageSalarioByDepartamento(departamento.getId());

        assertTrue(salarioPromedio.isPresent());
        assertEquals(5500000.0, salarioPromedio.get());
    }

}
