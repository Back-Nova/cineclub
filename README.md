

## Eentragable 1 - 

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
- Spring Data JPA(Hibernate)
- PostgreSQL 15
- Flyway (Migraciones de base de datos)
- Spring Security
- OpenAPI/Swagger (Documentación)
- Spring Boot Actuator



## Instalacion 

### 1. Levantar la base de datos


### docker compose up -d


Esto creará un contenedor PostgreSQL con:
- Usuario: `cineclub_user`
- Contraseña: `cineclub_pass`
- Base de datos: `cineclub_db`
- Puerto: `5432`

### 2. Ejecutar la aplicación

```bash
./mvnw spring-boot:run
```

La aplicación estará disponible en: `http://localhost:8080`

##  Endpoints 
### Películas (`/api/movies`)

- `GET /api/movies` - Listar todas las películas (paginado)
- `GET /api/movies?titulo=batman` - Buscar por título
- `GET /api/movies/{id}` - Obtener película por ID
- `POST /api/movies` - Crear nueva película
- `PUT /api/movies/{id}` - Actualizar película
- `DELETE /api/movies/{id}` - Eliminar película

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

## Postman

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


## Ejecutar Tests

```bash
./mvnw test
```

Para ver cobertura de tests:

```bash
./mvnw test jacoco:report
```



```bash
# Limpiar y recrear la base de datos
docker-compose down -v
docker-compose up -d
```
