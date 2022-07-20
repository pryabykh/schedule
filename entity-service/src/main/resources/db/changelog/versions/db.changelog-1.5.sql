--liquibase formatted sql

--changeset entity-service:1
ALTER TABLE classrooms RENAME COLUMN in_charge TO teacher;
--rollback ALTER TABLE classrooms RENAME COLUMN teacher TO in_charge;