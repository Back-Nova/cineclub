-- Esquema para tickets y reservas temporales (holds)

CREATE TABLE IF NOT EXISTS ticket (
    id              BIGSERIAL PRIMARY KEY,
    user_id         BIGINT NOT NULL,
    screening_id    BIGINT NOT NULL REFERENCES funcion(id) ON DELETE CASCADE,
    status          VARCHAR(32) NOT NULL,
    created_at      TIMESTAMP NOT NULL DEFAULT NOW(),
    expires_at      TIMESTAMP NULL,
    purchased_at    TIMESTAMP NULL
);

CREATE TABLE IF NOT EXISTS ticket_seat (
    id           BIGSERIAL PRIMARY KEY,
    ticket_id    BIGINT NOT NULL REFERENCES ticket(id) ON DELETE CASCADE,
    seat_id      BIGINT NOT NULL REFERENCES asiento_sala(id) ON DELETE RESTRICT
);



