# Retrospectiva

## Uso de Kanban

Durante el desarrollo del proyecto Incidents-Kanban, la metodología Kanban fue de gran ayuda para organizar el trabajo y visualizar el avance de cada actividad. Al utilizar un tablero con columnas como Product Backlog, Ready, In Progress, Review, Testing y Done, fue posible identificar el estado de cada tarea en todo momento. Esto permitió distribuir mejor el trabajo entre los integrantes y evitar que varias tareas importantes quedaran pendientes al mismo tiempo.

## Límite WIP

Uno de los principales retos fue trabajar con el límite WIP (Work In Progress). En algunos momentos surgió la necesidad de avanzar en varias tareas simultáneamente; sin embargo, el límite establecido obligó a terminar una actividad antes de iniciar otra. Aunque al principio esto pareció una restricción, con el tiempo se comprobó que ayudó a mantener el enfoque, reducir el trabajo incompleto y mejorar la calidad de las entregas.

## Pruebas y TDD

El uso de TDD (Test Driven Development) permitió detectar varios errores durante el desarrollo. Entre ellos se encontraron problemas en el cálculo automático de la prioridad de las incidencias, validaciones incorrectas en las transiciones de estado y algunos inconvenientes relacionados con la persistencia de datos en SQLite. Gracias a las pruebas unitarias fue posible identificar estos errores antes de integrar nuevas funcionalidades, lo que facilitó corregirlos oportunamente y evitar que afectaran otras partes del sistema.

## Refactorización

Durante el proyecto también fue necesario realizar procesos de refactorización. Se reorganizaron algunas clases para mejorar la separación de responsabilidades, se optimizaron nombres de métodos y variables para hacer el código más legible y se eliminaron fragmentos de código repetido. Estas mejoras permitieron mantener una estructura más limpia y facilitar el mantenimiento futuro del proyecto, sin modificar el comportamiento esperado del sistema.

## Cambio de requerimiento: EXPEDITE

En una etapa del desarrollo se presentó un cambio de requerimiento, consistente en incorporar la clase de servicio EXPEDITE para el manejo de incidencias prioritarias. Este cambio obligó a revisar el modelo de dominio, la lógica de negocio, la persistencia en SQLite y la interfaz gráfica. Aunque implicó trabajo adicional, la arquitectura implementada permitió integrar esta nueva funcionalidad sin afectar significativamente el resto del sistema.

## Uso de Inteligencia Artificial

La utilización de herramientas de Inteligencia Artificial, como ChatGPT y asistentes de programación integrados en el entorno de desarrollo, representó un apoyo importante durante el proyecto. La IA ayudó a generar ideas de implementación, resolver dudas relacionadas con Java, Maven, GitHub Actions y SQLite, además de colaborar en la elaboración de documentación, generación de ejemplos y revisión de código. Esto permitió reducir el tiempo invertido en tareas repetitivas y concentrar más esfuerzo en la lógica del sistema.

No obstante, la IA también presentó algunas limitaciones. En varias ocasiones generó código que requería ajustes para adaptarse a la estructura específica del proyecto o proponía soluciones incompatibles con la arquitectura implementada. Por esta razón fue necesario revisar cuidadosamente cada sugerencia, comprender su funcionamiento y realizar las modificaciones correspondientes antes de incorporarla al proyecto.

## Mejoras futuras

Como mejora para una siguiente versión, sería conveniente agregar nuevas funcionalidades como autenticación de usuarios, administración de roles, notificaciones automáticas, generación de reportes y una base de datos más robusta para escenarios con múltiples usuarios. Asimismo, sería recomendable ampliar la cobertura de pruebas automatizadas y continuar fortaleciendo la integración continua para garantizar una mayor calidad del software en futuras iteraciones.

## Conclusión

En general, la aplicación de Kanban, TDD e Inteligencia Artificial permitió desarrollar un proyecto organizado, funcional y alineado con las buenas prácticas de ingeniería de software.
