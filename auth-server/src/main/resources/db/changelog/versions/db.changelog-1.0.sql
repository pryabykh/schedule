--liquibase formatted sql

--changeset auth-server:1
CREATE TABLE tokens (
    id bigint PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    email varchar(255) NOT NULL UNIQUE,
    token text NOT NULL,
    created_at timestamp NOT NULL,
    updated_at timestamp NOT NULL
);
--rollback drop table tokens;