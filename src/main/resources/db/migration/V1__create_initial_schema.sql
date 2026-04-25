-- 1. Tabel Roles (Paling mandiri)
CREATE TABLE roles (
    id BIGSERIAL PRIMARY KEY,
    status VARCHAR(50) UNIQUE NOT NULL
);

-- 2. Tabel Users (Bergantung pada Roles)
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL,
    password VARCHAR(255) NOT NULL,
    role_id BIGINT,
    created_at TIMESTAMPTZ NOT NULL,
    CONSTRAINT fk_user_role FOREIGN KEY (role_id) REFERENCES roles(id)
);

-- 3. Tabel Refresh Tokens (Bergantung pada Users)
CREATE TABLE refresh_tokens (
    id BIGSERIAL PRIMARY KEY,
    token VARCHAR(255) UNIQUE NOT NULL,
    created_at TIMESTAMPTZ NOT NULL,
    expiry_date TIMESTAMPTZ NOT NULL,
    user_id BIGINT NOT NULL,
    CONSTRAINT fk_refresh_token_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Tambahin data awal untuk Roles
INSERT INTO roles (status) VALUES ('USER'), ('ADMIN'), ('TELLER');