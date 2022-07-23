--liquibase formatted sql

--changeset entity-service:1
CREATE TABLE classroom_subject (
    classroom_id bigint REFERENCES classrooms (id) ON UPDATE CASCADE ON DELETE CASCADE,
    subject_id bigint REFERENCES subjects (id) ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT classroom_subject_pkey PRIMARY KEY (classroom_id, subject_id)
);
--rollback drop table classroom_subject;