# 🧬 Mutant Detector API

API REST para detectar mutantes mediante análisis de secuencias de ADN.

## 📋 Estructura del Proyecto

```
src/
├── main/
│   ├── java/org/example/
│   │   ├── MutantDetectorApplication.java   # Clase principal Spring Boot
│   │   ├── controller/
│   │   │   └── MutantController.java        # REST endpoints
│   │   ├── dto/
│   │   │   ├── DnaRequest.java              # DTO Request
│   │   │   └── StatsResponse.java           # DTO Response
│   │   ├── entity/
│   │   │   └── DnaRecord.java               # Entidad JPA
│   │   ├── repository/
│   │   │   └── DnaRecordRepository.java     # Repository
│   │   ├── service/
│   │   │   ├── MutantDetector.java          # Algoritmo de detección
│   │   │   ├── MutantService.java           # Lógica de negocio
│   │   │   └── StatsService.java            # Estadísticas
│   │   ├── config/
│   │   │   └── SwaggerConfig.java           # Configuración Swagger
│   │   ├── exception/
│   │   │   ├── GlobalExceptionHandler.java  # Manejo global de errores
│   │   │   ├── ErrorResponse.java           # DTO Error
│   │   │   └── InvalidDnaException.java     # Excepción custom
│   │   └── validation/
│   │       ├── ValidDnaSequence.java        # Anotación custom
│   │       └── DnaSequenceValidator.java    # Validador
│   └── resources/
│       └── application.properties           # Configuración
└── test/
    └── java/org/example/
        ├── service/
        │   ├── MutantDetectorTest.java      # 17 tests unitarios
        │   ├── MutantServiceTest.java       # 5 tests con mocks
        │   └── StatsServiceTest.java        # 6 tests con mocks
        └── controller/
            └── MutantControllerTest.java    # 8 tests de integración
```

## 🚀 Ejecución

### Compilar el proyecto

```bash
.\gradlew.bat clean build
```

### Ejecutar tests

```bash
.\gradlew.bat test
```

### Generar reporte de cobertura

```bash
.\gradlew.bat jacocoTestReport
```

Reporte disponible en: `build/reports/jacoco/test/html/index.html`

### Ejecutar aplicación

```bash
.\gradlew.bat bootRun
```

### Generar JAR

```bash
.\gradlew.bat bootJar
```

JAR generado en: `build/libs/ExamenMercado-1.0-SNAPSHOT.jar`

## 📊 Coverage

-   **Total Tests**: 36 tests
-   **MutantDetectorTest**: 17 tests unitarios
-   **MutantServiceTest**: 5 tests con mocks
-   **StatsServiceTest**: 6 tests con mocks
-   **MutantControllerTest**: 8 tests de integración

## 🌐 Endpoints

### POST /mutant

Detecta si un ADN es mutante.

**Request:**

```json
{
	"dna": ["ATGCGA", "CAGTGC", "TTATGT", "AGAAGG", "CCCCTA", "TCACTG"]
}
```

**Response:**

-   `200 OK` - Es mutante
-   `403 FORBIDDEN` - Es humano
-   `400 BAD REQUEST` - ADN inválido

### GET /stats

Obtiene estadísticas de verificaciones.

**Response:**

```json
{
	"count_mutant_dna": 40,
	"count_human_dna": 100,
	"ratio": 0.4
}
```

## 📚 Documentación

-   **Swagger UI**: http://localhost:8080/swagger-ui.html
-   **H2 Console**: http://localhost:8080/h2-console

### Arquitectura

-   ✅ 6 capas: controller, dto, service, repository, entity, config
-   ✅ Dependency Injection con @RequiredArgsConstructor
-   ✅ DTOs separados (Request/Response)
-   ✅ Repository Pattern
-   ✅ Lombok (6 anotaciones)
-   ✅ GlobalExceptionHandler
-   ✅ Validaciones custom

### Algoritmo

-   ✅ Early Termination
-   ✅ Conversión a char[][]
-   ✅ Boundary Checking
-   ✅ Direct Comparison
-   ✅ Validation Set O(1)
-   ✅ Complejidad O(N²) con early termination
-   ✅ Complejidad espacial O(1)

### Testing

-   ✅ 36 tests totales
-   ✅ Tests unitarios
-   ✅ Tests con mocks
-   ✅ Tests de integración
-   ✅ Cobertura >80%

### API REST

-   ✅ POST /mutant
-   ✅ GET /stats
-   ✅ Swagger/OpenAPI
-   ✅ Validaciones

### Persistencia

-   ✅ H2 Database
-   ✅ JPA/Hibernate
-   ✅ Índices en BD
-   ✅ Caché de consultas

## 🛠️ Tecnologías

-   Java 17
-   Spring Boot 3.2.0
-   Spring Data JPA
-   H2 Database
-   Lombok
-   Swagger/OpenAPI
-   JUnit 5
-   Mockito
-   JaCoCo
-   Gradle 8.14
