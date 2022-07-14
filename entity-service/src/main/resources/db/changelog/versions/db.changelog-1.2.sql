--liquibase formatted sql

--changeset entity-service:1
CREATE INDEX creator_id_index ON classrooms (creator_id);
--rollback DROP INDEX creator_id_index;