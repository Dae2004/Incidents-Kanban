# HelpDesk Flow

HelpDesk Flow is a Java 21 desktop application for incident management.

The project is currently prepared for Phase 1 development with Maven, Swing, SQLite, and JUnit 5. Business logic and persistence behavior are intentionally not implemented yet.

## Build and test

```text
mvn clean test
```

## Architecture

The source tree is organized into model, service, repository, persistence, controller, view, validator, exception, and util packages under `com.helpdeskflow`.
