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