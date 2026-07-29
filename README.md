# HelpDesk Flow

HelpDesk Flow is a Java desktop application for registering, prioritizing, tracking, and analyzing support incidents through a Kanban-oriented workflow.

## Purpose

The application provides a small, self-contained help-desk tool. Users can register incidents, assign impact and urgency, obtain an automatic priority, move incidents through controlled states, search and filter the backlog, review metrics, and identify incidents marked with the EXPEDITE class of service.

## Technologies

- Java 21 (the Maven compiler is configured with `release 21`).
- Apache Maven 3.9.16 was used for the final local validation; Maven 3.9 or newer is recommended.
- JavaFX for the desktop graphical user interface.
- SQLite through Xerial `sqlite-jdbc` 3.46.1.0.
- JUnit Jupiter 5.10.3 for unit and persistence tests.
- GitHub Actions with Temurin Java 21 for continuous integration.

## Architecture

The application follows a layered design with MVC responsibilities:

- `model`: domain entities and enumerations.
- `service`: business use cases, priority calculation, state validation, EXPEDITE handling, and metrics.
- `repository`: persistence abstraction plus in-memory and SQLite/JDBC implementations.
- `persistence`: SQLite connection and schema initialization.
- `controller`: adapts user-interface actions to application services.
- `view`: JavaFX panels and the main application window.
- `validator`: input validation at the application boundary.
- `exception`: application-specific persistence and state-transition exceptions.

The `IncidentRepository` interface keeps services independent from the storage mechanism. `IncidentService` can therefore use the in-memory repository for isolated use cases or `IncidentRepositoryJdbc` for the desktop application.

## Package structure

```text
com.helpdeskflow
├── controller
├── exception
├── model
├── persistence
├── repository
├── service
├── validator
└── view
```

## Project structure

```text
.
├── .github/workflows/ci.yml       # GitHub Actions workflow
├── src/main/java                   # Production code
├── src/main/resources              # SQLite database and image resource locations
├── src/test/java                   # JUnit 5 tests
├── pom.xml                         # Maven build and dependencies
├── README.md                       # Project guide
├── IA-LOG.md                       # Implementation log
└── RETROSPECTIVA.md                # Final retrospective
```

The runtime database is created at `src/main/resources/database/helpdeskflow.db` when the default JDBC repository is initialized. Database files are ignored by Git.

## Installation

1. Install a JDK 21 distribution and verify it with `java -version`.
2. Install Apache Maven 3.9 or newer and verify it with `mvn -version`.
3. Clone the repository and change into its directory.
4. No separate database server or configuration file is required; SQLite is provided as a Maven dependency.

## Build and execute

Compile, package, and run the complete Maven verification lifecycle:

```bash
mvn clean verify
```

Launch the JavaFX application (modular, no warnings):

```bash
mvn javafx:run
```

Alternatively, run `com.helpdeskflow.Launcher` from an IDE configured with the Maven dependencies and a Java 21 SDK. A graphical desktop session is required for JavaFX.

## Unit tests

Run all tests:

```bash
mvn test
```

The test suite covers the domain model, priority matrix, incident registration, state transitions, queries and filters, metrics, EXPEDITE behavior, input validation, and SQLite repository operations.

## Continuous Integration

`.github/workflows/ci.yml` defines the **Java Continuous Integration** workflow. It runs on pushes and pull requests targeting `main` or `feature/**`, uses `ubuntu-latest` with Temurin Java 21, enables Maven dependency caching, and executes:

```text
mvn clean verify
```

Compilation or test failures make the workflow fail.

## Main implemented features

- Incident registration with required-field validation.
- Automatic priority calculation from impact and urgency.
- Controlled workflow: Registered → Ready → In Development → In Validation → Finished.
- Queries for all, open, closed, status, and priority-based incidents.
- SQLite persistence with a repository abstraction and an in-memory implementation for isolated use cases.
- Dashboard metrics for totals, status, priority, and average lead time when timestamps are available.
- EXPEDITE class-of-service registration and filtering.
- JavaFX interface for incident entry, list/detail views, and metrics.
- Automated build and test execution through GitHub Actions.

## Screenshots

> **Screenshot placeholder:** add a screenshot of the incident registration/list screen here before the final presentation if visual evidence is required.
>
> **Screenshot placeholder:** add a screenshot of the metrics tab here before the final presentation if visual evidence is required.

## Authors

- Dae2004
- Sebastian09192

## Academic delivery notes

The final branch contains documentation and API documentation updates only. Business logic, GUI behavior, persistence behavior, and tests remain unchanged from the completed implementation phases.
