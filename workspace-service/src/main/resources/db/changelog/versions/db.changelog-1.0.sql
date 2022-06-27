--liquibase formatted sql

--changeset workspace:1
CREATE TABLE workspaces (
    id bigint PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    name varchar(255) NOT NULL,
    creator bigint REFERENCES users (id) ON DELETE SET NULL,
    version integer NOT NULL,
    created_at timestamp NOT NULL,
    updated_at timestamp NOT NULL
);
--rollback drop table workspaces;