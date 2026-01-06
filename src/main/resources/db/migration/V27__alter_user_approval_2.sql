CREATE TYPE user_status_enum AS ENUM ('APPROVED', 'PENDING', 'REJECTED');

ALTER TABLE usuarios
    DROP COLUMN aprovado;

ALTER TABLE usuarios
    ADD COLUMN user_status user_status_enum NOT NULL DEFAULT 'PENDING';
