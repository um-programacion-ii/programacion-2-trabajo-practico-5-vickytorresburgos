# Prompts de Desarrollo

## Prompt 1: Dudas de Excepciones

### Prompt Utilizado: 

¿es una buena practica poner el mensaje de la excepcion cuando se implementa o cuando se define la excepcion?

### Respuesta Recibida: 

La mejor práctica es manejar los mensajes de error cuando se lanza la excepción (es decir, en la implementación) y no cuando se define la clase de excepción. Aquí te explico por qué:

Definición de la excepción (❌ No es la mejor práctica incluir mensajes fijos):
```
Java
public class DepartamentoNoEncontradoException extends RuntimeException {
// No es recomendable tener un mensaje fijo aquí
public DepartamentoNoEncontradoException() {
super("El departamento no fue encontrado"); // Mensaje estático
}
}
```
Definición correcta de la excepción (✅ Mejor práctica):

```
public class DepartamentoNoEncontradoException extends RuntimeException {
public DepartamentoNoEncontradoException(String mensaje) {
super(mensaje);
}


```
Implementación donde se usa la excepción (✅ Mejor práctica):

```
@Override
public Departamento buscarPorId(Long id) {
return departamentoRepository.findById(id)
.orElseThrow(() -> new DepartamentoNoEncontradoException(
"No se encontró el departamento con ID: " + id
));
}

```
Las razones por las que es mejor práctica definir los mensajes al lanzar la excepción son:

Flexibilidad:

Permite personalizar el mensaje según el contexto
Puede incluir datos específicos del error (IDs, valores, etc.)
Reutilización:

La misma excepción puede usarse en diferentes situaciones
Cada situación puede tener su propio mensaje descriptivo
Mantenibilidad:

Es más fácil modificar los mensajes sin cambiar la clase de excepción
Permite internacionalización más fácilmente
Claridad:

El mensaje describe exactamente qué falló en ese contexto específico
Puede incluir información de depuración relevante
Ejemplo de diferentes usos de la misma excepción:

```
// En buscarPorId
throw new DepartamentoNoEncontradoException("No se encontró el departamento con ID: " + id);
```
```
// En buscarPorNombre
throw new DepartamentoNoEncontradoException("No se encontró el departamento con nombre: " + nombre);
```
```
// En actualizar
throw new DepartamentoNoEncontradoException("No se puede actualizar - departamento no encontrado: " + id);
```

Esta aproximación hace que:

El código sea más flexible
Los mensajes de error sean más útiles
El mantenimiento sea más sencillo
La depuración sea más efectiva

### Modificaciones Realizadas: 

Maneje los mensajes de error cuando se lanza la excepcion

### Explicacion del Prompt: 

Use ese prompt porque mas alla de que se pueda definir el mensaje de la excepion cuando esta se define, queria saber cual es la mejor practica para aplicar en estos casos

### Aprendizajes Obtenidos: 
Al manejar los mensajes en la definicion de la excepcion, el mensaje queda fijo, por lo cual, no se podria personalizar el mensaje segun el contexto o incluir datos especificos del error.
Tambien es mas facil de mantener, es facil de reutilizar y de usar un mensaje descriptivo en cada situacion

## Prompt 2: Dudas de lanzado de escepciones en java

### Prompt Utilizado: ¿de que sirve declarar que un metodo lanza una excepcion asi 
```
public void eliminar(Long id, Departamento departamento) throws DepartamentoNoEncontradoException y ademas lanzarlo con un condicional de este modo         if (!departamentoRepository.existsById(id)) {
throw new DepartamentoNoEncontradoException("No se encontró el departamento con el ID: " + id);
}
```

La declaración `throws DepartamentoNoEncontradoException` en la firma del método y el lanzamiento explícito de la excepción dentro del método sirven propósitos diferentes, aunque relacionados:

1. **Declaración `throws` en la firma del método**:
```java
public void eliminar(Long id, Departamento departamento) throws DepartamentoNoEncontradoException
```
- Es una forma de documentación
- Hace explícito que el método puede lanzar esta excepción
- Es obligatorio SOLO para excepciones checked (que heredan de Exception)
- Es opcional para excepciones unchecked (que heredan de RuntimeException)

2. **Lanzamiento dentro del método**:
```java
if (!departamentoRepository.existsById(id)) {
    throw new DepartamentoNoEncontradoException("No se encontró el departamento con el ID: " + id);
}
```
- Es la implementación real del control de error
- Define cuándo y con qué mensaje se lanza la excepción

En este caso específico, como `DepartamentoNoEncontradoException` debería ser una unchecked exception (heredar de RuntimeException), **no es necesario** declarar el `throws` en la firma del método.

Aquí la versión corregida:

```java
@Override
public void eliminar(Long id, Departamento departamento) { // Sin throws
    if (!departamentoRepository.existsById(id)) {
        throw new DepartamentoNoEncontradoException("No se encontró el departamento con el ID: " + id);
    }
    departamentoRepository.delete(departamento);
}
```

Para clarificar:

1. **Para excepciones unchecked (RuntimeException)**:
- No es necesario declarar `throws`
- El compilador no obliga a capturarlas
- Se usan para errores de programación o condiciones que no se pueden recuperar
```java
public class DepartamentoNoEncontradoException extends RuntimeException {
    public DepartamentoNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}
```

2. **Para excepciones checked (Exception)**:
- Es obligatorio declarar `throws`
- El compilador obliga a capturarlas o propagarlas
- Se usan para condiciones excepcionales recuperables
```java
public class DepartamentoException extends Exception { // Checked exception
    public DepartamentoException(String mensaje) {
        super(mensaje);
    }
}
```

En tu caso específico, al ser una excepción de "no encontrado", debería ser unchecked (RuntimeException) y por lo tanto no necesitas el `throws` en la firma del método.

La regla general es:
- Usa excepciones unchecked (RuntimeException) para errores de programación y condiciones irrecuperables
- Usa excepciones checked (Exception) solo cuando el error puede ser manejado y recuperado por el código que llama al método

### Modificaciones Realizadas:

Modifique el metodo eliminar y revise las excepciones en los demas metodos

### Explicacion del Prompt:

Use ese prompt porque queria saber la diferencia entre lanzar la excepcion en la declaracion del metodo y lanzarla luego en la implementacion

### Aprendizajes Obtenidos:

Diferencia entre checked y unchecked exceptions y cuando se utiliza cada una

## Prompt 3: Más dudas de excepciones

### Prompt Utilizado: ¿que quiere decir que una excepcion extiende de RuntimeException o de Exception?

### Respuesta Recibida:

Cuando una excepción extiende de `RuntimeException` o `Exception`, determina su comportamiento y cómo el compilador requiere que sean manejadas. Aquí la explicación:

1. **Exception (Checked Exceptions)**
```java
public class MiException extends Exception {  // Checked Exception
    public MiException(String mensaje) {
        super(mensaje);
    }
}
```
- **DEBEN** ser declaradas en la firma del método con `throws`
- **DEBEN** ser manejadas con try-catch o propagadas
- El compilador **TE OBLIGA** a manejarlas
- Se usan para errores que se pueden recuperar
- Ejemplo de uso:
```java
// DEBE declararse con throws
public void metodoConCheckedException() throws MiException {
    if (algunaCondicion) {
        throw new MiException("Error recuperable");
    }
}

// DEBE manejarse con try-catch
try {
    metodoConCheckedException();
} catch (MiException e) {
    // Manejar el error
}
```

2. **RuntimeException (Unchecked Exceptions)**
```java
public class MiRuntimeException extends RuntimeException {  // Unchecked Exception
    public MiRuntimeException(String mensaje) {
        super(mensaje);
    }
}
```
- **NO** necesitan ser declaradas con `throws`
- **NO** necesitan ser manejadas con try-catch
- El compilador **NO TE OBLIGA** a manejarlas
- Se usan para errores de programación o irrecuperables
- Ejemplo de uso:
```java
// NO necesita throws en la declaración
public void metodoConRuntimeException() {
    if (algunaCondicion) {
        throw new MiRuntimeException("Error irrecuperable");
    }
}

// NO necesita try-catch (aunque puedes usarlo si quieres)
metodoConRuntimeException();
```

Ejemplo práctico comparando ambos tipos:

```java
// 1. RuntimeException - Para errores que NO deberían recuperarse
public class DepartamentoNoEncontradoException extends RuntimeException {
    public DepartamentoNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}

// 2. Exception - Para errores que SÍ deberían recuperarse
public class ArchivoNoDisponibleException extends Exception {
    public ArchivoNoDisponibleException(String mensaje) {
        super(mensaje);
    }
}

public class EjemploServicio {
    // Con RuntimeException - No necesita throws ni try-catch
    public void eliminarDepartamento(Long id) {
        if (!existeDepartamento(id)) {
            throw new DepartamentoNoEncontradoException("Departamento no encontrado");
        }
        // código para eliminar...
    }

    // Con Exception - NECESITA throws y try-catch
    public void leerArchivo(String ruta) throws ArchivoNoDisponibleException {
        if (!archivoExiste(ruta)) {
            throw new ArchivoNoDisponibleException("Archivo no disponible");
        }
        // código para leer archivo...
    }
}

// Uso de ambos métodos
public class Main {
    public void ejemplo() {
        EjemploServicio servicio = new EjemploServicio();

        // RuntimeException - No necesita try-catch
        servicio.eliminarDepartamento(1L);

        // Exception - DEBE usar try-catch
        try {
            servicio.leerArchivo("ruta/archivo.txt");
        } catch (ArchivoNoDisponibleException e) {
            // Manejar el error recuperable
            System.out.println("El archivo no está disponible, intentando alternativa...");
        }
    }
}
```

Reglas generales para decidir qué usar:
1. Usa `RuntimeException` cuando:
    - El error es culpa del programador (null pointer, índice fuera de rango)
    - No hay forma razonable de recuperarse del error
    - El error indica un bug en el código

2. Usa `Exception` cuando:
    - El error es esperado en ciertas circunstancias
    - El código que llama puede recuperarse del error
    - Quieres forzar el manejo del error

### Modificaciones Realizadas:

Revise las excepciones que ya tenia implementadas

### Explicacion del Prompt:

Use este prompt porque no sabia cuando una excepcion debe extender RuntimeException y Exception


### Aprendizaje Obtenido

Una excepcion debe extender de RuntimeException cuando no hay forma de recuperarse del error o hay un bug en el codigo
y debe extender de Exception cuando es un error esperable y recuperable

## Prompt 4: Duda de ubicacion de archivo GlobalExceptionHandler

### Prompt Utilizado: el GlobalExceptionHandler deberia ir en la carpeta de exceptions o de controllers?

### Respuesta Recibida:

La clase `GlobalExceptionHandler` debe ir en la carpeta **exceptions**.

### Razones:
- Es una **clase transversal**: aunque maneja errores de los controladores, su objetivo es centralizar el manejo de excepciones personalizadas y globales, no ser un controlador REST.
- Es una **buena práctica de organización**: ayuda a mantener el paquete `controllers` enfocado solo en los controladores de negocio (REST endpoints).
- Es donde normalmente pones todas tus excepciones personalizadas y el código relacionado al manejo de errores.

---

**Estructura recomendada:**
```
src/main/java/com/example/tp5/
  |-- controllers/
  |     |-- EmpleadoController.java
  |     |-- DepartamentoController.java
  |     |-- ProyectoController.java
  |
  |-- exceptions/
        |-- GlobalExceptionHandler.java
        |-- EmpleadoNoEncontradoException.java
        |-- ProyectoNoEncontradoException.java
        |-- DepartamentoNoEncontradoException.java
        |-- EmailDuplicadoException.java
```

---

**¿Resumen?**  
Pon `GlobalExceptionHandler` en el paquete `exceptions`.

¿Te gustaría ver el contenido completo de un buen `GlobalExceptionHandler` de ejemplo?

### Modificaciones Realizadas:

Mantuve el archivo en la carpeta exceptions

### Explicacion del Prompt:

Use este prompt porque no sabia donde debe ubicarse el archivo GlobalExceptionHandler

### Aprendizaje Obtenido

el archivo GlobalExceptionHandler debe ubicarse en la carpeta exceptions y no en controllers, porque en los controllers van solo los controladores para mantener una estructura coherente y ordenada

## Prompt 5: ¿que beneficio tiene hacerlo de esta manera? [codigo de ExceptionHandler]

### Respuesta Recibida:

¡Excelente pregunta!  
Hacerlo de esta manera, es decir, **agrupar varias excepciones relacionadas bajo un solo método `@ExceptionHandler`**, tiene varios beneficios importantes en el contexto de una API REST con Spring Boot:

---

### **Beneficios**

#### 1. **Centralización y Menos Duplicación de Código**
- Si cada excepción tuviera su propio método handler, terminarías copiando el mismo cuerpo de método muchas veces.
- Al agrupar, **escribes el formato de respuesta una sola vez** para todas las excepciones de “no encontrado” (“not found”).

#### 2. **Respuesta Consistente**
- Garantizas que **todas las excepciones de tipo “no encontrado”** devolverán una respuesta idéntica en cuanto a estructura y formato, lo que es muy importante en APIs REST públicas o internas.
- El cliente siempre puede esperar el mismo tipo de JSON, sin importar si el error fue por empleado, departamento o proyecto.

#### 3. **Fácil de Mantener**
- Si en el futuro quieres agregar más información a la respuesta, o cambiar el mensaje, **solo tienes que modificarlo en un lugar**.
- Si agregas otra excepción de “no encontrado”, solo la añades al array de clases del `@ExceptionHandler`.

#### 4. **Simplicidad**
- El código queda **más limpio y fácil de leer** y seguir.
- Menos métodos, menos líneas de código, mayor claridad.

#### 5. **Customización en un solo punto**
- Puedes personalizar el mensaje de error (“Recurso no encontrado”), el formato de la respuesta, o la lógica de registro/logging **en un solo sitio**.

---

### **¿Cuándo NO conviene?**
- Si necesitas que cada excepción tenga un tratamiento muy distinto (por ejemplo, diferente formato de JSON o lógica adicional), entonces sí deberías separarlas.

---

### **Resumen**
Agrupar excepciones similares en un solo handler:
- Reduce duplicación,
- Mejora la consistencia de respuestas,
- Facilita mantenimiento,
- Hace tu código más claro.

**¡Es una muy buena práctica para APIs REST en Spring!**

¿Quieres un ejemplo de comparación entre el enfoque agrupado y el individual?

### Modificaciones Realizadas:

Agrupé las excepciones del mismo tipo

### Explicación del Prompt:

Use ese prompt porque queria saber para que servia manejar las excepciones de esa manera