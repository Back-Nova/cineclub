# Guía de Pruebas con Postman - Entregable 1

## Configurar 


1. **Levantar la base de datos:**
```bash
docker compose up -d
```

2. **Ejecutar la aplicación:**
```bash
./mvnw spring-boot:run
```

3. **Verificar que esté corriendo:**
- API: http://localhost:8080
- Swagger UI: http://localhost:8080/swagger-ui.html
- Health Check: http://localhost:8080/actuator/health

## Endpoints****

### 1. PELÍCULAS (Movies)

#### 1.1 Crear Película
```
POST http://localhost:8080/api/movies
Content-Type: application/json

{
  "titulo": "Batman",
  "sinopsis": "Batman y robin",
  "duracionMinutos": 120,
  "clasificacionEdad": "PG-18",
  "fechaEstreno": "2008-07-18"
}
```

#### 1.2 Listar Peliculas (Paginado)
```
GET http://localhost:8080/api/movies?page=0&size=10
```

#### 1.3 Buscar Peliculas por Titulo
```
GET http://localhost:8080/api/movies?titulo=batman
```

#### 1.4 Obtener Película por ID
```
GET http://localhost:8080/api/movies/1
```

#### 1.5 Actualizar Película
```
PUT http://localhost:8080/api/movies/1
Content-Type: application/json

{
  "titulo": "The Dark Knight Updated",
  "sinopsis": "Updated synopsis",
  "duracionMinutos": 152,
  "clasificacionEdad": "PG-13",
  "fechaEstreno": "2008-07-18"
}
```

#### 1.6 Eliminar Película
```
DELETE http://localhost:8080/api/movies/1
```

---

### 2. SALAS (Rooms)

#### 2.1 Crear Sala
```
POST http://localhost:8080/api/rooms
Content-Type: application/json

{
  "nombre": "Sala 1",
  "capacidad": 100
}
```

#### 2.2 Listar Salas
```
GET http://localhost:8080/api/rooms?page=0&size=10
```

#### 2.3 Buscar Salas por Nombre
```
GET http://localhost:8080/api/rooms?nombre=sala
```

#### 2.4 Obtener Sala por ID
```
GET http://localhost:8080/api/rooms/1
```

#### 2.5 Actualizar Sala
```
PUT http://localhost:8080/api/rooms/1
Content-Type: application/json

{
  "nombre": "Sala 1 VIP",
  "capacidad": 80
}
```

#### 2.6 Eliminar Sala
```
DELETE http://localhost:8080/api/rooms/1
```

---

### 3. BUTACAS 

#### 3.1 Crear Butaca en una Sala
```
POST http://localhost:8080/api/rooms/1/seats
Content-Type: application/json

{
  "filaLabel": "A",
  "numeroAsiento": 1
}
```

#### 3.2 Listar Butacas de una Sala (Paginado)
```
GET http://localhost:8080/api/rooms/1/seats?page=0&size=20
```

#### 3.3 Listar Todas las Butacas de una Sala 
```
GET http://localhost:8080/api/rooms/1/seats/all
```

#### 3.4 Obtener Butaca por ID
```
GET http://localhost:8080/api/rooms/1/seats/1
```

#### 3.5 Actualizar Butaca
```
PUT http://localhost:8080/api/rooms/1/seats/1
Content-Type: application/json

{
  "filaLabel": "A",
  "numeroAsiento": 2
}
```

#### 3.6 Eliminar Butaca
```
DELETE http://localhost:8080/api/rooms/1/seats/1
```

---

### 4. FUNCIONES 

#### 4.1 Crear Función
```
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

#### 4.2 Listar Funciones
```
GET http://localhost:8080/api/screenings?page=0&size=10
```

#### 4.3 Filtrar Funciones por Película
```
GET http://localhost:8080/api/screenings?peliculaId=1
```

#### 4.4 Filtrar Funciones por Sala
```
GET http://localhost:8080/api/screenings?salaId=1
```

#### 4.5 Filtrar Funciones por Rango de Fechas
```
GET http://localhost:8080/api/screenings?start=2024-12-01T00:00:00&end=2024-12-31T23:59:59
```

#### 4.6 Obtener Función por ID
```
GET http://localhost:8080/api/screenings/1
```

#### 4.7 Actualizar Función
```
PUT http://localhost:8080/api/screenings/1
Content-Type: application/json

{
  "peliculaId": 1,
  "salaId": 1,
  "horaInicio": "2024-12-01T20:00:00",
  "horaFin": "2024-12-01T22:30:00",
  "estado": "ACTIVA"
}
```

#### 4.8 Eliminar Función
```
DELETE http://localhost:8080/api/screenings/1
```

---

##  Ejemplo 

### Paso 1: Crear Datos de Prueba

1. **Crear Película:**
```json
POST http://localhost:8080/api/movies
{
  "titulo": "Avatar",
  "sinopsis": "Epic",
  "duracionMinutos": 100,
  "clasificacionEdad": "PG-19",
  "fechaEstreno": "2007-11-1"
}
```


2. **Crear Sala:**
```json
POST http://localhost:8080/api/rooms
{
  "nombre": "Sala Principal",
  "capacidad": 150
}
```


3. **Crear Butacas es mejor hacer mas :**
```json
POST http://localhost:8080/api/rooms/1/seats
{
  "filaLabel": "A",
  "numeroAsiento": 1
}

POST http://localhost:8080/api/rooms/1/seats
{
  "filaLabel": "A",
  "numeroAsiento": 2
}

POST http://localhost:8080/api/rooms/1/seats
{
  "filaLabel": "B",
  "numeroAsiento": 1
}
```

4. **Crear Funcion:**
```json
POST http://localhost:8080/api/screenings
{
  "peliculaId": 1,
  "salaId": 1,
  "horaInicio": "2024-12-15T19:00:00",
  "horaFin": "2024-12-15T21:42:00",
  "estado": "ACTIVA"
}
```


### Paso 2: Validaciones de Negocio

1. **Intentar crear función con horario erroneo:**
```json
POST http://localhost:8080/api/screenings
{
  "peliculaId": 1,
  "salaId": 1,
  "horaInicio": "2024-12-15T20:00:00",
  "horaFin": "2024-12-15T22:00:00",
  "estado": "ACTIVA"
}
```
Respuesta esperada: `409 Conflict` - 

2. **Intentar crear película duplicada:**
```json
POST http://localhost:8080/api/movies
{
  "titulo": "Avatar",

}
```
Respuesta esperada: `400 Bad Request` - "Ya existe una película "

3. **Intentar crear función con hora fin antes de hora inicio:**
```json
POST http://localhost:8080/api/screenings
{
  "peliculaId": 1,
  "salaId": 1,
  "horaInicio": "2024-12-15T20:00:00",
  "horaFin": "2024-12-15T18:00:00",
  "estado": "ACTIVA"
}
```
Respuesta esperada: `409 Conflict` - "La hora de fin debe ser posterior a la hora de inicio"
