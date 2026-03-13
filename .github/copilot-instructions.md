# Copilot Instructions — eux-relaterte-rinasaker

## Build & Test

```bash
# Build all modules (uses Maven Daemon)
mvnd clean install

# Build with standard Maven (CI uses this)
mvn clean install --settings ./.github/settings.xml --no-transfer-progress -B

# Run a single test class
mvnd test -pl eux-relaterte-rinasaker-webapp -Dtest=RelaterteRinasakerApiImplTest

# Run a single test method
mvnd test -pl eux-relaterte-rinasaker-webapp -Dtest="RelaterteRinasakerApiImplTest#POST relaterterinasaker søk - søk uten kriterier - 200"

# Run the app locally
make run
```

Tests require a running PostgreSQL 17 instance:

```bash
export DATABASE_HOST=localhost DATABASE_PORT=5432 DATABASE_DATABASE=postgres DATABASE_USERNAME=postgres DATABASE_PASSWORD=postgres
```

## Architecture

Kotlin Spring Boot service for managing groups of related RINA cases (rinasaker). Used by EUX NEESSI to link and navigate between related cases.

### Module dependency flow

```
openapi → (generates API interfaces)
model → persistence → service → webapp
```

- **openapi** — OpenAPI YAML specs under `src/main/resources/static/`. The `kotlin-spring` generator produces Spring API interfaces (interface-only, no implementations) and request/response types into `src/gen/java/main/`.
- **model** — JPA entities and domain DTOs (Kotlin data classes). Contains the core `RelaterteRinasakerGruppe` entity.
- **persistence** — Spring Data JPA repository. Flyway migrations under `src/main/resources/db/migration/`.
- **service** — Business logic with `@Transactional` on write operations. Uses `kotlin-logging` (`logger {}`).
- **webapp** — `@RestController` implementations of generated API interfaces, mappers, exception advice, security config, and the Spring Boot application entry point.

### API-first design

API contracts are defined in OpenAPI YAML before code. The generated `RelaterteRinasakerApi` interface is implemented by `RelaterteRinasakerApiImpl`. Never edit generated code under `target/generated-sources/` — modify the YAML specs instead.

## Key Conventions

### Kotlin with one Java exception

All source code is Kotlin except `SecurityConfig.java` (Spring Security filter chain).

### Three-layer type conversion

API types (generated `*Type` classes) → domain DTOs (`*Request`/`*Patch`/`*SearchCriteria`) → JPA entities. Conversions use Kotlin extension functions and extension properties in mapper files:

```kotlin
// Extension property for list conversion
val List<RelaterteRinasakerCreateType>.relaterteRinasakerGruppeCreateRequestList
    get() = map { it.toRelaterteRinasakerGruppeCreateRequest() }

// Extension function for single-object conversion
fun RelaterteRinasakerGruppe.toRelaterteRinasakerType() = RelaterteRinasakerType(...)
```

### Fluent controller style

Controllers chain mapper calls and response entity helpers in a single expression:

```kotlin
override fun relaterteRinasakerSearch(...) = service
    .search(criteria.relaterteRinasakerSearchCriteria)
    .toRelaterteRinasakerType()
    .toRelaterteRinasakerSearchResponseType()
    .toOkResponseEntity()
```

### Entity immutable patching

Entities define a `patch()` method that returns a new copy with updated fields, preserving unchanged values via null-coalescing:

```kotlin
fun patch(patch: Patch) = copy(beskrivelse = patch.beskrivelse ?: beskrivelse, ...)
```

### Constructor injection

Services and controllers use Kotlin primary constructor injection (no `@Autowired`):

```kotlin
@Service
class RelaterteRinasakerService(val repository: RelaterteRinasakerRepository)
```

### Testing

Integration tests use `@SpringBootTest` with `RANDOM_PORT`, `@EnableMockOAuth2Server`, and `@AutoConfigureRestTestClient`. Tests use `RestTestClient` with fluent assertions. Database is cleaned via `@BeforeEach` with `JdbcTestUtils.deleteFromTables`. Test names are descriptive Norwegian sentences using backtick syntax.

### Database

PostgreSQL 17 with Flyway migrations. Two tables: `relaterte_rinasaker_gruppe` (UUID PK) and `relaterte_rinasaker` (element collection). UUIDs for all primary keys.

### Authentication

OAuth2 JWT via Azure AD. All endpoints require authentication except `/actuator/health`, `/actuator/prometheus`, and Swagger UI paths.
