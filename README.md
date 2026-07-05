Library Management Service (just for interview)

## Running project
You can run the project using maven or docker.

### Option 1: Running with Docker
* If you already pulled postgres or pgadmin, open `docker-compose.yml` and replace full image name with postgres.image or pgadmin.image.
* Also you don't have to run pgadmin.
* Its better to create .env file at the root directory and set all these environment variables:
```bash
POSTGRES_PORT=5432
POSTGRES_USER=admin
POSTGRES_PASSWORD=admin
POSTGRES_DB=interview_db

PGADMIN_DEFAULT_PORT=5050
PGADMIN_DEFAULT_EMAIL=admin@mail.com
PGADMIN_DEFAULT_PASSWORD=admin
```
Running command
```bash
$ docker compose up -d # Run all services including pgadmin
$ docker compose up postgres interview-app -d # Just run postgres and backend service 
```

### Option 2: Running with Maven
* With maven, you only can run the backend service, You can still run postgres by `docker-compose.yml` or use 
your preconfigured postgres (in that case, you should create a database and paste the name of database, 
postgres username and postgres password in src/main/resources/config/application.properties)
* Run the final jar file using java 17
2. Run the following command:
```bash
   $ ./mvnw clean package -DskipTests
   $ java -jar target/interview-0.0.1-SNAPSHOT.jar
```
---

## Requirements

* JDK 17
* Maven 3.8 or Docker

---

## Technologies

* JDK 17
* Spring Boot 3.3.2
* Spring Data JPA (Hibernate)
* PostgreSQL
* JUnit 5 and Mockito
* MapStruct
* Lombok
* Spring AOP
* Spring Security & Auditing
* Swagger UI
* Testcontainers

---
## Project Structure
* **aop.logging**: Contains logging logic for every bean in application's main package, also all repositories, services and RestControllers on `after throwing` and also `around` using aspect.
* **common.exception**: Define a general error response and Handles all errors based on Local and resource bundle using RestControllerAdvice
* **configuration**: contains all configuration classes including security, audit system, openapi and message source
* **domain**: contains all entities across the project including base entity and others
* **repository**: contains all repositories across the project
* **model**: Contains all domain specific models such as enums across project
* **service**: Contains contract of application services, their implementation, DTOs, mappers, specifications of services and business exceptions
* **web.rest**: Contains controllers
* **resources**: Contains project config, resource bundle and data.sql, schema.sql to initialize database if data cleared
* **test**: unit test on book service and integration test on book and loan service
---

## Some Important Design Decisions

1. **Implementing an Automated Auditing System**: Since multiple operators can work with the software at the same time in a library, 
   I implemented an auditing mechanism using a mapped superclass AbstractAuditingEntity with Spring Security's AuditorAware,
   so we can always track exactly which operator deleted a book or created a loan record, 
   along with the exact timestamp, without writing manual logic in the services.

2. **Using Testcontainers for integration test**: Because integration tests need a database to run and using in memory database can cause issue while production
   database is postgres, So I decided to use Testcontainers to run postgres instance while integration tests started.

3. **Standardizing API Response Format**: Because clients need a fixed and consistent structure to handle responses,
   I created a generic ErrorResponse class to wrap all error messages,
   and a PagedResponseDto record to wrap paginated results instead of returning Spring's Page directly,
   so the frontend can rely on the same response shape even if the Spring Data version or its internal Page structure changes.

---

## Future Improvements

1. Expanding the Book Entity: I would add more columns to the book table, such as page count, genre of books, 
   publisher, etc... and extend the JPA Specification to allow dynamic querying based on these new attributes.
2. Normalizing the Database (Author Entity): Instead of storing the author's name as a plain string inside the Book table, 
   I would prefer to create a separate Author entity and establish a relationship (e.g., Many-to-One) using an
   author_id.