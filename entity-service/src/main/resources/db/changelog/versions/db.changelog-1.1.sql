--liquibase formatted sql

--changeset entity-service:1
CREATE TABLE classrooms (
    id bigint PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    number varchar(255) NOT NULL UNIQUE,
    capacity integer NOT NULL,
    description varchar(255),
    creator_id bigint NOT NULL,
    version integer NOT NULL,
    created_at timestamp NOT NULL,
    updated_at timestamp NOT NULL
);
--rollback drop table classrooms;