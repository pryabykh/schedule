--liquibase formatted sql

--changeset entity-service:1
CREATE TABLE teachers (
    id bigint PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    first_name varchar(255) NOT NULL,
    patronymic varchar(255),
    last_name varchar(255) NOT NULL,
    creator_id bigint NOT NULL,
    version integer NOT NULL,
    created_at timestamp NOT NULL,
    updated_at timestamp NOT NULL
);
--rollback drop table teachers;