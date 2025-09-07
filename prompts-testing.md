# Prompts de Testing

## Prompt 1: Configuración de los tests para diferentes bases de datos (h2, mysql y postgres)

### Prompt Utilizado: 
como puedo configurar los tests para que funcionen en las diferentes bases de datos que estoy utilizando en mi proyecto? (h2, mysql y postgresql)


### Respuesta Recibida:

Muy buena pregunta 👌
Lo que querés es **probar que tu aplicación funciona igual en las tres bases de datos (H2, MySQL y PostgreSQL)**, porque a veces hay diferencias en los dialectos o en cómo manejan las consultas SQL.

### 🔧 Estrategia para configurar tests multi–base de datos

---

#### 1. Definir **profiles** en tu proyecto

En `src/main/resources/` creá tres archivos `application-*.yml` (o `.properties`), uno por cada base de datos:

**🔹 `application-h2.yml`**

```yaml
spring:
  datasource:
    url: jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;MODE=PostgreSQL
    driver-class-name: org.h2.Driver
    username: sa
    password:
  jpa:
    hibernate:
      ddl-auto: create-drop
    show-sql: true
    database-platform: org.hibernate.dialect.H2Dialect
```

> 🔑 Nota: `MODE=PostgreSQL` ayuda a que H2 imite mejor a Postgres en tests.

---

**🔹 `application-mysql.yml`**

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/testdb
    driver-class-name: com.mysql.cj.jdbc.Driver
    username: root
    password: root
  jpa:
    hibernate:
      ddl-auto: create-drop
    show-sql: true
    database-platform: org.hibernate.dialect.MySQL8Dialect
```

---

**🔹 `application-postgres.yml`**

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/testdb
    driver-class-name: org.postgresql.Driver
    username: postgres
    password: postgres
  jpa:
    hibernate:
      ddl-auto: create-drop
    show-sql: true
    database-platform: org.hibernate.dialect.PostgreSQLDialect
```

---

#### 2. Usar `@ActiveProfiles` en los tests

En tus tests, podés activar el profile correspondiente:

```java
@SpringBootTest
@ActiveProfiles("h2")
class EmpleadoServiceH2Test { ... }

@SpringBootTest
@ActiveProfiles("mysql")
class EmpleadoServiceMySQLTest { ... }

@SpringBootTest
@ActiveProfiles("postgres")
class EmpleadoServicePostgresTest { ... }
```

---

#### 3. Compartir lógica de tests

En lugar de duplicar todo, podés crear una clase abstracta con los **tests comunes**, y extenderla con los perfiles:

```java
abstract class EmpleadoServiceBaseTest {
    @Autowired
    protected EmpleadoService empleadoService;
    
    @Test
    void testGuardarEmpleado() { ... }
    
    @Test
    void testBuscarPorId() { ... }
}
```

Y luego:

```java
@SpringBootTest
@ActiveProfiles("h2")
class EmpleadoServiceH2Test extends EmpleadoServiceBaseTest {}

@SpringBootTest
@ActiveProfiles("mysql")
class EmpleadoServiceMySQLTest extends EmpleadoServiceBaseTest {}

@SpringBootTest
@ActiveProfiles("postgres")
class EmpleadoServicePostgresTest extends EmpleadoServiceBaseTest {}
```

---

#### 4. (Opcional) Automatizar con **Testcontainers**

Si no querés levantar manualmente MySQL/Postgres, podés usar **Testcontainers**, que levanta contenedores de Docker para cada DB en tiempo de test:

```java
@Testcontainers
@SpringBootTest
class EmpleadoServicePostgresContainerTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("testdb")
            .withUsername("postgres")
            .withPassword("postgres");

    @DynamicPropertySource
    static void configure(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }
}
```

---

✅ Con esto vas a poder correr **los mismos tests en las tres bases**, y detectar si alguna consulta funciona en una DB pero rompe en otra.

---

¿Querés que te arme el **ejemplo con Testcontainers** para las tres bases de datos (H2, MySQL y PostgreSQL) en un mismo archivo de test parametrizado?



### Modificaciones Realizadas:
- cree una clase abstracta donde testeo de la capa de repositorios de cada entidad y luego la extendi con los diferentes perfiles 
- Opte por esta opción porque me parecio la forma mas limpia e intuitiva de testar la capa de repositorios con diferentes profiles y sin repetir codigo

### Explicación del Prompt:
Use este prompt porque queria saber como testear con diferentes profiles de bases de datos

### Aprendizajes Obtenidos:
- como testear mi aplicacion con diferentes bases de datos extendiendo una clase abstracta

