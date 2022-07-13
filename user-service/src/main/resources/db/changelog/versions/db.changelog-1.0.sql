--liquibase formatted sql

--changeset user-service:1
CREATE TABLE users (
    id bigint PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    email varchar(255) NOT NULL UNIQUE,
    password varchar(255) NOT NULL,
    first_name varchar(255) NOT NULL,
    last_name varchar(255) NOT NULL,
    version integer NOT NULL,
    created_at timestamp NOT NULL,
    updated_at timestamp NOT NULL
);
--rollback drop table users;