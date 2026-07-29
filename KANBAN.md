# Tablero Kanban — HelpDesk Flow

Este archivo representa el tablero Kanban documental del proyecto. Las tarjetas reflejan el alcance implementado durante las fases 1 a 12 y no constituyen nuevas funcionalidades.

## Flujo de trabajo y límite WIP

```text
Product Backlog → Ready → In Progress (WIP = 2) → Review → Testing → Done
```

**Límite visible:** la columna **In Progress** admite como máximo 2 tarjetas simultáneas. En el estado actual no hay tarjetas activas en esa columna; el trabajo implementado se encuentra en `Done`.

## Estado actual del tablero

### Product Backlog

_Sin tarjetas pendientes del alcance aprobado._

### Ready

_Sin tarjetas listas para iniciar._

### In Progress (WIP = 2)

_0/2 tarjetas activas._

### Review

_Sin tarjetas pendientes de revisión._

### Testing

_Sin tarjetas pendientes de validación._

### Done

#### Historias de usuario

| ID | Historia de usuario | Resultado |
|---|---|---|
| HU-01 | Como usuario, quiero registrar incidencias para documentar problemas de soporte. | Implementada |
| HU-02 | Como usuario, quiero consultar incidencias para conocer el trabajo registrado. | Implementada |
| HU-03 | Como usuario, quiero filtrar incidencias para encontrar rápidamente las que necesito. | Implementada |
| HU-04 | Como usuario, quiero cambiar el estado de una incidencia para controlar su avance. | Implementada |
| HU-05 | Como usuario, quiero visualizar métricas para analizar el comportamiento del flujo. | Implementada |
| HU-06 | Como usuario, quiero calcular la prioridad automáticamente para ordenar la atención. | Implementada |
| HU-07 | Como usuario, quiero persistir incidencias en SQLite para conservar la información. | Implementada |
| HU-08 | Como usuario, quiero una interfaz gráfica para operar el sistema de forma sencilla. | Implementada |
| HU-09 | Como usuario, quiero marcar incidencias como EXPEDITE para distinguir la atención urgente. | Implementada |

#### Tareas técnicas

| ID | Tarea técnica | Resultado |
|---|---|---|
| TT-01 | Diseñar el modelo de dominio y sus enumeraciones. | Completada |
| TT-02 | Aplicar Repository Pattern con implementaciones en memoria y JDBC. | Completada |
| TT-03 | Integrar SQLite, conexión JDBC y creación del esquema. | Completada |
| TT-04 | Construir la GUI Swing con vistas, formularios y panel de métricas. | Completada |
| TT-05 | Configurar GitHub Actions con Java 21, caché Maven y `mvn clean verify`. | Completada |
| TT-06 | Ejecutar la refactorización general y organizar paquetes. | Completada |
| TT-07 | Completar README, bitácora, retrospectiva y documentación de API. | Completada |

#### Defectos encontrados y corregidos

| ID | Defecto | Resolución |
|---|---|---|
| BUG-01 | Se permitían transiciones de estado que no seguían el orden del flujo. | Se incorporó validación explícita de estados consecutivos. |
| BUG-02 | La prioridad no se derivaba consistentemente de impacto y urgencia. | Se centralizó el cálculo en una matriz de prioridad. |
| BUG-03 | La persistencia no diferenciaba correctamente registros inexistentes al actualizar. | El repositorio informa el error mediante `PersistenceException`. |

#### Cambio de requerimiento

| ID | Cambio | Resolución |
|---|---|---|
| CR-01 | Se solicitó incorporar la clase de servicio EXPEDITE durante el desarrollo. | Se añadió `ClassOfService.EXPEDITE`, su servicio de registro/consulta y su cobertura de pruebas. |

## Historial de movimiento de tarjetas

La siguiente trazabilidad muestra el movimiento progresivo aplicado a las tarjetas principales:

| Tarjetas | Recorrido realizado | Estado final |
|---|---|---|
| HU-01, HU-02, HU-03 | Product Backlog → Ready → In Progress → Review → Testing → Done | Done |
| HU-04, HU-05, HU-06 | Product Backlog → Ready → In Progress → Review → Testing → Done | Done |
| HU-07, HU-08 | Product Backlog → Ready → In Progress → Review → Testing → Done | Done |
| HU-09, CR-01 | Product Backlog → Ready → In Progress → Review → Testing → Done | Done |
| TT-01 a TT-07 | Product Backlog → Ready → In Progress → Review → Testing → Done | Done |
| BUG-01 a BUG-03 | In Progress → Review → Testing → Done | Done |

## Criterios de terminado

Una tarjeta se consideró terminada cuando su implementación estaba integrada, tenía pruebas o validación técnica apropiada, no introducía cambios fuera de su alcance y podía pasar la verificación Maven completa. Para la documentación final se añadió además la revisión del README, la bitácora, la retrospectiva y el tablero.
