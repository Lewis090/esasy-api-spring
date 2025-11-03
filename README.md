# easy-api-spring

Quick start and troubleshooting for local development.

## Run with Docker (recommended)

Start Postgres and app together:

```bash
docker-compose up --build
```

The docker-compose file includes a healthcheck for Postgres and sets the containerized app JDBC URL to `jdbc:postgresql://postgres-db:5432/easyapi?sslmode=disable` to avoid SSL negotiation issues in local/dev.

## Run app locally (IDE / Maven)

If you run the Spring Boot app locally on your host, ensure Postgres is running (through Docker or installed locally) and update `src/main/resources/application.properties` accordingly. The project includes default properties for local runs:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/easyapi?sslmode=disable
spring.datasource.username=admin
spring.datasource.password=admin123
```

Then run:

```bash
mvn spring-boot:run
```

## Troubleshooting

- EOFException during SSL handshake: make sure `sslmode=disable` is present in the JDBC URL for local/dev runs where Postgres is not configured for SSL.
- DB name mismatch: docker-compose creates DB `easyapi`. Make sure the JDBC URL uses that name (not `easy-api`).
- Check container status:

```bash
docker ps
docker-compose ps
docker logs postgres-db
```

- Check Postgres readiness:

```bash
docker exec -it postgres-db pg_isready -U admin -d easyapi
```

If you want I can add a simple retry-on-startup in the Spring Boot app to wait for DB readiness before failing; say the word and I will implement it.

