# Quarkus Hello World

A minimal [Quarkus](https://quarkus.io/) REST API built with Maven.

## Prerequisites

- Java 17+
- Maven 3.9+

## Running the application

### Dev mode

```bash
mvn quarkus:dev
```

The app starts at http://localhost:8080 with live reload enabled.

### Production mode

```bash
mvn package
java -jar target/quarkus-1.0.0-SNAPSHOT-runner.jar
```

## API

| Method | Path    | Response           |
|--------|---------|--------------------|
| GET    | `/hello` | `Hello World 1.0` |
| GET    | `/bye`   | `Bye World`       |

## Tests

```bash
mvn test
```

## Project structure

```
src/main/java/com/example/HelloResource.java   REST endpoints
src/test/java/com/example/HelloResourceTest.java   Integration tests
pom.xml   Maven build and Quarkus dependencies
```
