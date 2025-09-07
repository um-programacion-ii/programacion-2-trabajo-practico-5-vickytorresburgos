package com.example.tp5.repositories.departamento;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("mysql")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class DepartamentoRepositoryMysqlTest extends AbstractDepartamentoRepositoryTest {
}