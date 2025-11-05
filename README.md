

## Entregable 1 - Fundaciones

Este entregable incluye:
- CRUD completo de Películas (Movie)
- CRUD completo de Salas (Room)
- CRUD completo de Butacas (RoomSeat)
- CRUD completo de Funciones (Screening)
- Validaciones de negocio
- Documentación OpenAPI/Swagger
- Tests unitarios

## Stack 

- Java 21 (LTS)
- Spring Boot 3.5.6
- Spring Data JPA (Hibernate)
- PostgreSQL 15
- Flyway (Migraciones de base de datos)
- Spring Security
- OpenAPI/Swagger (Documentación)
- Spring Boot Actuator



## Instalación

### Opción 1: Docker

Para dockerizar todo el proyecto:

```bash
# Construir y levantar ambos servicios (BD + App)
docker compose up -d --build

# Ver logs de la aplicación
docker compose logs -f app

# Detener servicios
docker compose down
```

La aplicación estará disponible en: `http://localhost:8080`

### Opción 2: Desarrollo Local 

### 1. Levantar solo la base de datos

```bash
docker compose up db -d
```

Esto creará un contenedor PostgreSQL con:
- Usuario: `cineclub_user`
- Contraseña: `cineclub_pass`
- Base de datos: `cineclub_db`
- Puerto: `5432`

### 2. Ejecutar la aplicación

```bash
./mvnw spring-boot:run
```


##  Endpoints 
### Películas (`/api/movies`)

- `GET /api/movies` - Listar todas las películas (paginado)
- `GET /api/movies?titulo=batman` - Buscar por título
- `GET /api/movies/{id}` - Obtener película por ID
- `POST /api/movies` - Crear nueva película
- `PUT /api/movies/{id}` - Actualizar película
- `DELETE /api/movies/{id}` - Eliminar película
- `POST /api/movies/search` - Buscar con filtros dinamicos (Specification + paginado)
```json
{
  "titulo": "matrix",
  "duracionMinima": 90,
  "duracionMaxima": 150
}
```
- `GET /api/movies/search` - Buscar con filtros dinámicos por query params (Specification + paginado)

### Salas (`/api/rooms`)

- `GET /api/rooms` - Listar todas las salas (paginado)
- `GET /api/rooms?nombre=sala` - Buscar por nombre
- `GET /api/rooms/{id}` - Obtener sala por ID
- `POST /api/rooms` - Crear nueva sala
- `PUT /api/rooms/{id}` - Actualizar sala
- `DELETE /api/rooms/{id}` - Eliminar sala

### Butacas (`/api/rooms/{roomId}/seats`)

- `GET /api/rooms/{roomId}/seats` - Listar butacas de una sala
- `GET /api/rooms/{roomId}/seats/all` - Listar todas las butacas (sin paginación)
- `GET /api/rooms/{roomId}/seats/{id}` - Obtener butaca por ID
- `POST /api/rooms/{roomId}/seats` - Crear nueva butaca
- `PUT /api/rooms/{roomId}/seats/{id}` - Actualizar butaca
- `DELETE /api/rooms/{roomId}/seats/{id}` - Eliminar butaca

### Funciones (`/api/screenings`)

- `GET /api/screenings` - Listar todas las funciones (paginado)
- `GET /api/screenings?peliculaId=1` - Filtrar por película
- `GET /api/screenings?salaId=1` - Filtrar por sala
- `GET /api/screenings?start=2024-01-01T00:00:00&end=2024-12-31T23:59:59` - Filtrar por rango de fechas
- `GET /api/screenings/{id}` - Obtener función por ID
- `POST /api/screenings` - Crear nueva función
- `PUT /api/screenings/{id}` - Actualizar función
- `DELETE /api/screenings/{id}` - Eliminar función

## Entregable 2 - Disponibilidad y ciclo de tickets

Este entregable incluye:
- Disponibilidad de asientos por función con estados: AVAILABLE | HELD | SOLD
- Ciclo de tickets: creación de hold (TTL), confirmación de compra, cancelación
- Reglas de negocio: límites por usuario, umbral previo a función, ownership
- Endpoints protegidos por usuario (simulados vía header `X-User-Id` en dev)

### Endpoints (Entregable 2)

- `GET /api/screenings/{id}/availability` - Disponibilidad de asientos (AVAILABLE | HELD | SOLD)
- `POST /api/screenings/{id}/holds` - Crear hold (X-User-Id requerido)
- `POST /api/tickets/{ticketId}/confirm` - Confirmar compra (X-User-Id requerido)
- `DELETE /api/tickets/{ticketId}` - Cancelar hold propio (X-User-Id requerido)
- `GET /api/me/tickets` - Listar tickets/holds del usuario (X-User-Id requerido)

## Ejemplos 

### 1. Crear una Película

```json
POST http://localhost:8080/api/movies
Content-Type: application/json

{
  "titulo": "The Dark Knight",
  "sinopsis": "Batman fights the Joker",
  "duracionMinutos": 152,
  "clasificacionEdad": "PG-13",
  "fechaEstreno": "2008-07-18"
}
```

### 2. Crear una Sala

```json
POST http://localhost:8080/api/rooms
Content-Type: application/json

{
  "nombre": "Sala 1",
  "capacidad": 100
}
```

### 3. Crear Butacas para una Sala

```json
POST http://localhost:8080/api/rooms/1/seats
Content-Type: application/json

{
        "salaId":1,
  "filaLabel": "A",
  "numeroAsiento": 1
}
```

### 4. Crear una Función

```json
POST http://localhost:8080/api/screenings
Content-Type: application/json

{
  "peliculaId": 1,
  "salaId": 1,
  "horaInicio": "2024-12-01T19:00:00",
  "horaFin": "2024-12-01T21:30:00",
  "estado": "ACTIVA"
}
```

### 5. Buscar Películas con Specification (filtros dinámicos + paginado)

```json
POST http://localhost:8080/api/movies/search?page=0&size=10&sort=titulo,asc
Content-Type: application/json

{
  "titulo": "matrix",
  "sinopsis": "",
  "duracionMinima": 90,
  "duracionMaxima": 150,
  "clasificacionEdad": "PG-13",
  "fechaDesde": "1995-01-01",
  "fechaHasta": "2005-12-31"
}
```

### 6. Buscar Películas con Specification usando GET 

```bash
GET http://localhost:8080/api/movies/search?titulo=matrix&duracionMinima=90&duracionMaxima=150&clasificacionEdad=PG-13&fechaDesde=1995-01-01&fechaHasta=2005-12-31&page=0&size=10&sort=titulo,asc
```

### 7. Disponibilidad de asientos

```bash
GET http://localhost:8080/api/screenings/1/availability
```

Respuesta (200):
```json
{
  "screeningId": 1,
  "seats": [
    { "seatId": 101, "fila": "A", "numero": 1, "estado": "DISPONIBLE" },
    { "seatId": 102, "fila": "A", "numero": 2, "estado": "RESERVADA" },
    { "seatId": 103, "fila": "A", "numero": 3, "estado": "VENDIDA" }
  ]
}
```

### 8. Crear hold (reserva temporal) 

```json
POST http://localhost:8080/api/screenings/1/holds
X-User-Id: 42
Content-Type: application/json

{
  "seatIds": [101, 102],
  "ttlSeconds": 300
}
```

### 9. Confirmar compra

```bash
POST http://localhost:8080/api/tickets/555/confirm
X-User-Id: 42
```

### 10. Cancelar hold

```bash
DELETE http://localhost:8080/api/tickets/id_ticker
X-User-Id: 42
```

http://localhost:8080/api/screenings/1/availability

### 11. Mis tickets

```bash
GET http://localhost:8080/api/me/tickets?page=0&size=10&sort=purchasedAt,desc
X-User-Id: 42
```


## Ejecutar Tests

```bash
./mvnw test
```

Para ver cobertura de tests:

```bash
./mvnw test jacoco:report
```

##  Comandos Docker 

```bash
# Construir solo la imagen de la app
docker build -t cineclub-app .

# Ver logs de la aplicación
docker compose logs -f app

# Reiniciar solo la aplicación
docker compose restart app

# Entrar al contenedor de la BD
docker exec -it cineclub-db psql -U cineclub_user -d cineclub_db

# Ver estado de los contenedores
docker compose ps

# Limpiar y recrear todo
docker compose down -v
docker compose up -d --build
```
