# E-commerce API

The backend service for the e-commerce platform. It exposes REST endpoints for
authentication, users, products, categories, carts, orders, payments, reviews,
shipping, promotions, banners, inventory, reporting, and store settings.

The API is built with Spring Boot, Java 21, Spring MVC, Spring Data JPA,
PostgreSQL, Spring Security, JWT, OAuth2 Google login, Springdoc OpenAPI,
email, Telegram notifications, and Bakong KHQR payments.

## Requirements

- Java 21
- PostgreSQL 16, or Docker Desktop
- Maven is optional because the repository includes the Maven wrapper

## Configuration

The default local configuration is in
`src/main/resources/application.yaml`:

| Setting           | Local default                  |
| ----------------- | ------------------------------ |
| API URL           | `http://localhost:8081/api/v1` |
| PostgreSQL host   | `localhost:5433`               |
| Database          | `ecommerce`                    |
| Database user     | `ecommerce`                    |
| Database password | `ecommerce`                    |
| Upload limit      | `20 MB`                        |

The application also needs credentials for JWT, Google OAuth2, SMTP, Telegram,
and Bakong KHQR when those integrations are enabled. Keep all secrets outside
source control. Replace any credentials that may have been committed and
prefer environment-variable overrides or a local, ignored configuration file.

## Start PostgreSQL with Docker

From this directory:

```bash
docker compose up -d postgres
```

This starts PostgreSQL on port `5433` with database `ecommerce`, user
`ecommerce`, and password `ecommerce`.

## Run the API locally

```bash
./mvnw spring-boot:run
```

On Windows, use `mvnw.cmd spring-boot:run`.

The API is available at [http://localhost:8081/api/v1](http://localhost:8081/api/v1).
Swagger UI is available at
[http://localhost:8081/api/v1/swagger-ui/index.html](http://localhost:8081/api/v1/swagger-ui/index.html)
when the application is running.

## Build and test

```bash
./mvnw clean verify
```

To build without running tests:

```bash
./mvnw clean package -DskipTests
```

The packaged application is created in `target/`.

## Run the complete stack with Docker

```bash
docker compose up --build
```

The compose file starts PostgreSQL and the API. Uploaded product files are
stored in the local `uploads/products` directory.

## API groups

The API uses the `/api/v1` context path and includes these resource groups:

- `/auth` - Sign up, sign in, refresh tokens, OTP, password recovery, and current user
- `/users`, `/roles`, `/permissions` - Accounts and access control
- `/products`, `/categories`, `/inventory` - Catalog and stock
- `/cart` - Cart items
- `/orders`, `/payment`, `/shipments`, `/addresses` - Checkout and fulfillment
- `/banners`, `/promotions`, `/review` - Store content and customer feedback
- `/stats`, `/report`, `/audit`, `/settings` - Administration and reporting

Most protected endpoints require a bearer access token:

```http
Authorization: Bearer <access-token>
```

## Project structure

```text
src/main/java/.../config/       Application and security configuration
src/main/java/.../controller/   REST controllers
src/main/java/.../service/      Business logic
src/main/java/.../repository/   JPA repositories
src/main/java/.../entity/       Database entities
src/main/java/.../dto/          Request and response DTOs
src/main/resources/             Configuration and database resources
uploads/products/                Uploaded product images
```

## Frontend

The Next.js frontend is in `../ecommerce`. Its default API configuration points
to `http://localhost:8081/api/v1`. Start the frontend with `npm run dev` from
that directory.

## Operational notes

- `spring.jpa.hibernate.ddl-auto` is set to `update` for local development.
- Do not use the development credentials or `ddl-auto: update` in production.
- Configure a persistent upload location and backups before production use.
- Restrict CORS, OAuth redirect URLs, SMTP access, and payment credentials to
  the environments that need them.
