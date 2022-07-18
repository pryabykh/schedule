--liquibase formatted sql

--changeset entity-service:1
ALTER TABLE classrooms ADD COLUMN in_charge bigint REFERENCES teachers (id) ON DELETE SET NULL;
--rollback ALTER TABLE classrooms DROP COLUMN in_charge ;