# Final Retrospective

## What was achieved

The project evolved from an initial Maven skeleton into a complete desktop incident-management application. The implementation now includes a domain model, automatic prioritization, controlled workflow transitions, filtering, metrics, SQLite persistence, a Swing interface, EXPEDITE handling, and GitHub Actions CI.

## Technical decisions

- Java 21 provides the language and runtime baseline.
- Maven centralizes dependency management, compilation, testing, and packaging.
- Swing keeps the graphical client desktop-native without adding a separate UI runtime.
- SQLite provides lightweight local persistence with no external server requirement.
- The repository interface separates use cases from storage details and supports both JDBC and in-memory implementations.
- JUnit 5 tests protect the domain, service, and persistence behavior.
- GitHub Actions runs the same complete Maven verification lifecycle used locally.

## Practices applied

- MVC separates Swing views, controllers, and application services.
- Services own use-case orchestration while validators and calculators keep focused responsibilities.
- Domain and persistence errors are represented with meaningful exceptions.
- Documentation explains installation, execution, architecture, testing, and CI usage.
- The final audit excludes IDE files, build artifacts, local databases, credentials, and temporary files from delivery.

## Lessons learned

- Keeping business rules in services makes them testable without opening the GUI.
- A repository abstraction makes SQLite behavior testable and keeps storage replaceable.
- CI is most useful when it executes the same `mvn clean verify` command used by developers.
- Completing documentation after implementation exposes outdated assumptions and improves handoff quality.

## Final assessment

The repository is suitable for academic delivery. The remaining screenshot markers in `README.md` are intentional placeholders for presentation evidence and do not affect compilation, tests, or application behavior.
