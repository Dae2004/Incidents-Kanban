# HelpDesk Flow

## Descripción del sistema

HelpDesk Flow es un sistema de escritorio para la gestión de incidencias de soporte. Permite registrar incidencias, calcular automáticamente su prioridad a partir del impacto y la urgencia, controlar su avance mediante estados, consultar y filtrar el trabajo, visualizar métricas y persistir la información en SQLite.

La aplicación también contempla la clase de servicio **EXPEDITE** para identificar incidencias que requieren atención prioritaria dentro del flujo Kanban.

## Integrantes

- Dae2004
- Sebastian09192

## Tecnologías utilizadas

- **Java 21**: lenguaje y plataforma de ejecución.
- **Apache Maven 3.9.16**: gestión del proyecto, dependencias, compilación y pruebas.
- **Swing**: interfaz gráfica de escritorio.
- **SQLite** mediante Xerial `sqlite-jdbc` 3.46.1.0: persistencia local.
- **JUnit 5** (`junit-jupiter` 5.10.3): pruebas unitarias y de persistencia.
- **GitHub Actions**: integración continua.
- **JDBC**: acceso a la base de datos SQLite.
- **Git**: control de versiones.

## Requisitos de ejecución

- JDK 21 o superior compatible con la compilación `release 21`.
- Apache Maven 3.9 o superior.
- Sistema operativo con soporte para una aplicación Swing.
- Sesión gráfica disponible para ejecutar la interfaz.
- No se requiere un servidor de base de datos: SQLite se incluye como dependencia Maven.

Verificar las herramientas instaladas:

```bash
java -version
mvn -version
```

## Compilación

Desde la raíz del repositorio, descargar dependencias, limpiar artefactos anteriores, compilar y ejecutar el ciclo completo de verificación:

```bash
mvn clean verify
```

Para compilar sin ejecutar las pruebas:

```bash
mvn clean package -DskipTests
```

## Ejecución

La aplicación se inicia mediante la clase principal `com.helpdeskflow.HelpDeskFlowApplication`.

Con Maven:

```bash
mvn compile exec:java -Dexec.mainClass=com.helpdeskflow.HelpDeskFlowApplication
```

También se puede ejecutar `HelpDeskFlowApplication` desde un IDE, seleccionando un SDK Java 21 y las dependencias del proyecto Maven. Al iniciar la aplicación se crea el esquema SQLite si todavía no existe. La base de datos predeterminada se encuentra en `src/main/resources/database/helpdeskflow.db` y los archivos locales de base de datos están excluidos del control de versiones.

## Pruebas

Ejecutar todas las pruebas unitarias y de persistencia con:

```bash
mvn test
```

La validación recomendada para una entrega completa es:

```bash
mvn clean verify
```

La suite cubre el modelo de dominio, cálculo de prioridad, registro, transiciones, consultas, filtros, métricas, EXPEDITE, validación de entradas y operaciones del repositorio SQLite.

## Arquitectura

El sistema utiliza una arquitectura por capas con responsabilidades MVC y principios de Clean Architecture:

- **Model** (`model`): entidades `Incident`, `IncidentId` y enumeraciones del dominio.
- **View** (`view`): ventanas y paneles Swing.
- **Controller** (`controller`): adapta acciones de la interfaz a los servicios de aplicación.
- **Service** (`service`): casos de uso, cálculo de prioridad, transiciones, métricas y EXPEDITE.
- **Repository** (`repository`): abstracción de almacenamiento y sus implementaciones en memoria y JDBC.
- **Persistence** (`persistence`): conexiones y creación del esquema SQLite.
- **Validator** (`validator`): validación de datos de entrada.
- **Exception** (`exception`): errores específicos de persistencia y transiciones.

El **Repository Pattern** desacopla los servicios del mecanismo de almacenamiento. La separación de responsabilidades favorece **SOLID**, evita duplicación innecesaria y permite probar los servicios con un repositorio en memoria. La interfaz gráfica sigue MVC y la lógica de negocio permanece fuera de las vistas.

## Decisiones principales de diseño

- Se eligió Java Swing para mantener una aplicación de escritorio sencilla y sin un servidor adicional.
- Se eligió SQLite porque ofrece persistencia local portable y no requiere infraestructura externa.
- La prioridad se calcula en un componente dedicado mediante una matriz de impacto y urgencia.
- Las transiciones de estado se validan en un servicio específico para impedir saltos inválidos del flujo.
- La interfaz `IncidentRepository` permite intercambiar SQLite por un repositorio en memoria durante las pruebas.
- La clase de servicio EXPEDITE se modela explícitamente para conservar la trazabilidad de ese requerimiento.
- GitHub Actions ejecuta `mvn clean verify` para que una compilación o prueba fallida impida validar el cambio.

## Integración Continua

El workflow **Java Continuous Integration**, ubicado en `.github/workflows/ci.yml`, se ejecuta en cada `push` y `pull_request` dirigido a `main` o a ramas `feature/**`.

El workflow utiliza `ubuntu-latest`, Temurin Java 21 y caché de Maven. Después de descargar el repositorio y configurar Java, ejecuta:

```text
mvn clean verify
```

Por lo tanto, comprueba compilación, dependencias, empaquetado y todas las pruebas automáticamente.

## Tablero Kanban

El tablero versionado del proyecto está disponible en [KANBAN.md](KANBAN.md).
> **Tablero visual externo:** `[ENLACE AL TABLERO KANBAN]`

El archivo incluye las historias de usuario, tareas técnicas, defectos, el cambio de requerimiento EXPEDITE, el límite WIP y el historial de movimiento de las tarjetas.

## Estructura del proyecto

```text
.
├── .github/workflows/ci.yml       # Integración continua
├── src/main/java/com/helpdeskflow  # Código de producción
│   ├── controller                   # Controladores MVC
│   ├── exception                    # Excepciones de aplicación
│   ├── model                        # Modelo de dominio
│   ├── persistence                   # SQLite y JDBC
│   ├── repository                    # Repository Pattern
│   ├── service                       # Casos de uso y reglas
│   ├── validator                     # Validación de entradas
│   └── view                          # Interfaz Swing
├── src/main/resources                # Recursos y ubicación de SQLite
├── src/test/java                     # Pruebas JUnit 5
├── pom.xml                           # Configuración Maven
├── KANBAN.md                         # Tablero Kanban documental
├── IA-LOG.md                         # Registro de implementación
└── RETROSPECTIVA.md                  # Retrospectiva del proyecto
```

## Autores

- **Dae2004**
- **Sebastian09192**
