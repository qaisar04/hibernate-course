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

CREATE TABLE profile -- [base]
(
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL UNIQUE REFERENCES users (id),
    street VARCHAR(128),
    language CHAR(2)
);

CREATE TABLE company_locale -- не состоятельная сущность
(
    company_id INT NOT NULL REFERENCES company (id),
    lang CHAR(2) NOT NULL,
    description VARCHAR(128) NOT NULL,
    PRIMARY KEY (company_id, lang)
);

CREATE TABLE chat
(
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(64) NOT NULL UNIQUE
);

CREATE TABLE users_chat
(
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES users (id),
    chat_id BIGINT REFERENCES chat (id),
    created_at TIMESTAMP NOT NULL,
    created_by VARCHAR(128) NOT NULL,
    UNIQUE (user_id, chat_id )
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
DROP TABLE profile;
DROP TABLE users_chat;
