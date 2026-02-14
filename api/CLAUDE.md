# CLAUDE.md

## Quick Start

Essential commands for daily development:
```bash
./gradlew bootRun           # Run application

./gradlew test              # Run tests

./gradlew build             # Build project

./gradlew ktlintCheck       # Check code style

./gradlew ktlintFormat      # Format code
```

## Project Info

**Tech Stack**:
- Kotlin 1.9+
- Spring Boot 3.x
- Gradle 8.x

## Architecture

### Package Structure
```
api/src/main/kotlin/org/example/roulette/api/
├── common/          # Cross-cutting concerns (global response, exceptions)
├── [domain]/        # Business domains (user, roulette, point, etc.)
│   ├── api/         # REST controllers
│   ├── app/         # Services & DTOs  
│   └── domain/      # Entities & JPA repositories
```

**4-Layer Structure** (each domain):
- `api/` - Controllers (REST endpoints)
- `app/` - Services & DTOs (business logic)  
- `domain/` - Entities & Repositories (core models)
- `infra/` - External APIs (third-party integrations)


### Conventions & Patterns

- **API Response**: Consistent `ApiResponse<T>` wrapper
- **Naming**: PascalCase (classes), camelCase (methods), snake_case (DB)
- **Code Style**: ktlint defaults, data classes for DTOs