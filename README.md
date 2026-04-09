# Bookstore Backend

Spring Boot backend for the book e-commerce frontend.

## Tech stack

- Java 21+
- Spring Boot
- Spring Web
- Spring Data JPA
- MySQL
- Maven

## API endpoints

- `GET /api/health`
- `GET /api/books`
- `GET /api/books/{id}`
- `GET /api/books/categories`
- `POST /api/books`
- `PUT /api/books/{id}`
- `DELETE /api/books/{id}`

## Supported query params for `GET /api/books`

- `search`
- `category`
- `minPrice`
- `maxPrice`
- `minRating`
- `sortBy`

`sortBy` supports: `name`, `title`, `price-low`, `price-high`, `rating`

## MySQL setup

1. Create a schema named `bookstore_db` in MySQL Workbench, or let Spring create it with `createDatabaseIfNotExist=true`.
2. Update credentials through environment variables if needed:
   - `DB_URL`
   - `DB_USERNAME`
   - `DB_PASSWORD`
3. Start the app with `mvn spring-boot:run`.

On first startup, the app seeds the `books` table from `src/main/resources/seed/books.json`.

## Frontend integration note

To connect the existing React app later, point it at:

- `http://localhost:8080/api/books`
- `http://localhost:8080/api/books/{id}`
