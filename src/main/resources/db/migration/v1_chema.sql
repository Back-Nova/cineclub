-- Proyecto: Cineclub
-- Descripcion : Creacion de las tablas para pelicula,sala

CREATE TABLE pelicula (
    id SERIAL PRIMARY KEY,
    titulo VARCHAR(100) NOT NULL,
    sinopsis TEXT,
    duracion_minutos INT NOT NULL,
    clasificacion_edad VARCHAR(10),
    fecha_estreno DATE
);

CREATE TABLE sala (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL,
    capacidad INT NOT NULL
);

CREATE TABLE asiento_sala (
    id SERIAL PRIMARY KEY,
    sala_id INT REFERENCES sala(id) ON DELETE CASCADE,
    fila_label VARCHAR(10) NOT NULL,
    numero_asiento INT NOT NULL
);

CREATE TABLE funcion (
    id SERIAL PRIMARY KEY,
    pelicula_id INT REFERENCES pelicula(id) ON DELETE CASCADE,
    sala_id INT REFERENCES sala(id) ON DELETE CASCADE,
    hora_inicio TIMESTAMP NOT NULL,
    hora_fin TIMESTAMP NOT NULL,
    estado VARCHAR(20) DEFAULT 'ACTIVA'
);

CREATE index idx_funcion_pelicula ON funcion(pelicula_id);
CREATE index idx_funcion_sala ON funcion(sala_id);
CREATE index idx_asiento_sala_id ON asiento_sala(sala_id);

