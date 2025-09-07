# Prompts de Documentacion

## Prompt 1: Como documentar endpoints correctamente

### Prompt Utilizado: 
como puedo documentar los endpoints de mi aplicacion?

### Respuesta Recibida: 

Para documentar los endpoints de tu aplicación Spring Boot, lo más común y profesional es usar **Swagger / OpenAPI**. Te permite generar documentación interactiva automáticamente y probar los endpoints desde un navegador. Te explico paso a paso:

---

## 1️⃣ Agregar la dependencia

Si estás usando **Maven**, agregá en tu `pom.xml`:

```xml
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>2.1.0</version> <!-- usar la versión más reciente -->
</dependency>
```

> Si usás Spring WebFlux, hay otro starter, pero para la mayoría de los proyectos con Spring MVC, este es el adecuado.

---

## 2️⃣ Configuración mínima

Con Spring Boot moderno, **no hace falta casi nada**. Al iniciar la app, automáticamente se expone la UI de Swagger en:

```
http://localhost:8080/swagger-ui.html
```

Y el JSON de OpenAPI en:

```
http://localhost:8080/v3/api-docs
```

---

## 3️⃣ Documentar controladores y endpoints

Podés usar anotaciones de **Swagger / OpenAPI** (`@Operation`, `@Parameter`, `@ApiResponse`, etc.) para que la documentación sea más completa.

Ejemplo:

```java
@RestController
@RequestMapping("/api/proyectos")
public class ProyectoController {

    private final ProyectoService proyectoService;

    public ProyectoController(ProyectoService proyectoService) {
        this.proyectoService = proyectoService;
    }

    @GetMapping("/{id}")
    @Operation(
        summary = "Obtener proyecto por ID",
        description = "Devuelve un proyecto específico dado su ID"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Proyecto encontrado"),
        @ApiResponse(responseCode = "404", description = "Proyecto no encontrado")
    })
    public Proyecto obtenerPorId(@PathVariable Long id) {
        return proyectoService.buscarPorId(id);
    }

    @PostMapping
    @Operation(summary = "Crear un nuevo proyecto")
    public Proyecto crear(@RequestBody Proyecto proyecto) {
        return proyectoService.guardar(proyecto);
    }
}
```

> Con estas anotaciones, Swagger mostrará en la UI los endpoints, parámetros, respuestas y descripciones.

---

## 4️⃣ Personalizar la documentación

Podés crear un bean de configuración para agregar información global de la API:

```java
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de Gestión de Proyectos")
                        .version("1.0")
                        .description("Documentación de los endpoints de la aplicación"));
    }
}
```

---

## 5️⃣ Probar y compartir

1. Levantá la aplicación.
2. Entrá a `http://localhost:8080/swagger-ui.html`.
3. Ahí vas a ver todos los endpoints con sus métodos HTTP, parámetros y posibles respuestas.
4. Podés exportar la documentación como JSON/YAML para integrarlo con otras herramientas.

---

Si querés, te puedo armar **un ejemplo completo de controller + Swagger listo para tu ProyectoService**, con endpoints de CRUD y documentación detallada para cada uno. Esto te dejaría la base lista para toda tu app.

¿Querés que haga eso?

### Modificaciones Realizadas: 

- Agregue la dependencia de OpenAPI desde la pagina oficial
- Documente los endpoints indicando la operacion, la respuesta/s de la api y los parametros que recibe cada mtéodo

### Explicacion del Prompt: 
- Use ese prompt porque queria saber si existia algun otro modo de documentar el codigo aparte de con comentarios

### Aprendizajes Obtenidos: 
- Aprendi que se puede documentar de una forma mas limpia y ordenada que con comentarios