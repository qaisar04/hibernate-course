CREATE TABLE users -- [base]
(
    id BIGSERIAL PRIMARY KEY,
    firstname VARCHAR(128),
    lastname  VARCHAR(128),
    birth_date DATE,
    username VARCHAR(128) UNIQUE,
    role VARCHAR(32),
    info JSONB,
    company_id INT REFERENCES company (id)
--     company_id INT REFERENCES company (id) ON DELETE CASCADE
);

CREATE TABLE company
(
    id SERIAL PRIMARY KEY,
    name VARCHAR(64) NOT NULL UNIQUE
);

CREATE TABLE profile
(
    user_id BIGINT PRIMARY KEY REFERENCES users (id),
    street VARCHAR(128),
    language CHAR(2)
);

CREATE TABLE users
(
--  id BIGSERIAL PRIMARY KEY,
--  id BIGINT PRIMARY KEY,
    firstname VARCHAR(128),
    lastname  VARCHAR(128),
    birth_date DATE,
    username VARCHAR(128) UNIQUE,
    role VARCHAR(32),
    info JSONB,
    PRIMARY KEY (firstname, lastname, birth_date) -- NOT NULL AND UNIQUE
);

CREATE SEQUENCE users_id_seq
owned by users.id;

DROP TABLE users;