--liquibase formatted sql

--changeset entity-service:1
CREATE TABLE subjects (
    id bigint PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    name varchar(255) NOT NULL,
    first_grade integer,
    second_grade integer,
    third_grade integer,
    fourth_grade integer,
    fifth_grade integer,
    sixth_grade integer,
    seventh_grade integer,
    eighth_grade integer,
    ninth_grade integer,
    tenth_grade integer,
    eleventh_grade integer
);
--rollback drop table subjects;