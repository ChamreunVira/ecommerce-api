# Ecommerce API

Backend REST API for an ecommerce application, built with Spring Boot and PostgreSQL.
The API supports catalog management, authentication, shopping carts, checkout, orders,
shipping, inventory, promotions, reviews, reporting, and administration.

## Technology

- Java 21
- Spring Boot 4.0.6
- Spring MVC, Spring Data JPA, and Spring Security
- PostgreSQL 16
- JWT authentication and Google OAuth2 login
- Springdoc OpenAPI / Swagger UI
- Bakong KHQR payments
- Telegram notifications, email, caching, and audit logging

## Requirements

- Java 21 or newer
- Docker and Docker Compose for the containerized workflow
- PostgreSQL 16 if running the application directly

## Configuration

The application reads its default settings from `src/main/resources/application.yaml`.
Before sharing or deploying this project, move passwords, OAuth credentials, JWT secrets,
payment tokens, and bot tokens out of that file and provide them through environment
variables or a local, untracked profile.

The default local database configuration is:

```text
Host: localhost
Port: 5433
Database: ecommerce
Username: ecommerce
Password: ecommerce
```

The application listens on port `8081` and uses `/api/v1` as its context path. Useful
configuration values include:

```text
SPRING_DATASOURCE_URL
SPRING_DATASOURCE_USERNAME
SPRING_DATASOURCE_PASSWORD
JWT_BASE64SECRET
JWT_EXPIRATION
UPLOADS_DIR
```

The application also expects credentials when Google OAuth2, SMTP, Bakong KHQR, or
Telegram integrations are enabled.

## Run with Docker Compose

This starts PostgreSQL and builds the API image:

```bash
docker compose up --build
```

The API is available at `http://localhost:8081/api/v1` and PostgreSQL is exposed on
`localhost:5433`. Stop the services with:

```bash
docker compose down
```

To remove the persisted PostgreSQL volume as well:

```bash
docker compose down -v
```

## Run locally

Start PostgreSQL with the database and credentials shown above, then run:

```bash
./mvnw spring-boot:run
```

On Windows, use `mvnw.cmd` instead.

Build the executable JAR:

```bash
./mvnw clean package
java -jar target/ecommerce-api-0.0.1-SNAPSHOT.jar
```

Run the test suite with:

```bash
./mvnw test
```

## API documentation

With the application running, open:

- Swagger UI: `http://localhost:8081/api/v1/swagger-ui/index.html`
- OpenAPI JSON: `http://localhost:8081/api/v1/v3/api-docs`

## Main endpoint groups

All routes below are relative to `/api/v1`.

| Area                             | Base path                          |
| -------------------------------- | ---------------------------------- |
| Authentication                   | `/auth`                            |
| Products and categories          | `/products`, `/categories`         |
| Banners and store settings       | `/banners`, `/settings`            |
| Cart                             | `/cart`                            |
| Orders and payments              | `/orders`, `/payment`              |
| Shipping addresses and shipments | `/addresses`, `/shipments`         |
| Inventory                        | `/inventory`                       |
| Promotions                       | `/promotions`                      |
| Reviews                          | `/review`                          |
| Users, roles, and permissions    | `/users`, `/roles`, `/permissions` |
| Statistics and reports           | `/stats`, `/report`                |
| Audit logs                       | `/audit`                           |

Product creation and updates use `multipart/form-data` for image uploads. Payment
flows include KHQR generation, payment status lookup, and payment lookup by order.

## Project structure

```text
src/main/java/com/kh/vira_dev/ecommerceapi/
├── controller/   REST controllers
├── service/      Business services and checkout orchestration
├── entity/       JPA entities
├── repository/   Spring Data repositories
├── dto/          Request and response models
├── security/     Authentication and authorization
├── jwt/          JWT creation and validation
├── config/       Application and integration configuration
└── util/         File upload and shared utilities
```

Uploaded product images are stored under `uploads/products` by default. Keep that
directory mounted or backed up when running in a containerized environment.

## Data and checkout notes

- A shipping address is editable user data and can be marked as the default address.
- Checkout stores an address snapshot so a later address edit does not change an
  existing order.
- `Product` represents current catalog data, while `OrderItem` stores historical order
  data.

## License

No license has been specified for this project.
