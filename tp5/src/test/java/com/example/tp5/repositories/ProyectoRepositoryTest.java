package com.example.tp5.repositories;

import com.example.tp5.models.Proyecto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("h2")
public class ProyectoRepositoryTest {
    @Autowired
    private ProyectoRepository proyectoRepository;

    @Test
    void testFindProyectosActivos() {
        Proyecto proyectoActivo = new Proyecto();
        proyectoActivo.setNombre("Proyecto Activo");
        proyectoActivo.setFechaInicio(LocalDate.now().minusMonths(1));
        proyectoActivo.setFechaFin(LocalDate.now().plusMonths(6));
        proyectoRepository.save(proyectoActivo);

        Proyecto proyectoInactivo = new Proyecto();
        proyectoInactivo.setNombre("Proyecto Inactivo");
        proyectoInactivo.setFechaInicio(LocalDate.now().minusMonths(12));
        proyectoInactivo.setFechaFin(LocalDate.now().minusMonths(1));
        proyectoRepository.save(proyectoInactivo);

        List<Proyecto> proyectosActivos = proyectoRepository.findProyectosActivos(LocalDate.now());

        assertNotNull(proyectosActivos);
        assertEquals(1, proyectosActivos.size());
        assertEquals("Proyecto Activo", proyectosActivos.get(0).getNombre());
    }
}
