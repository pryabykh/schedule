--liquibase formatted sql

--changeset entity-service:1
CREATE INDEX creator_id_index ON classrooms (creator_id);
CREATE INDEX number_index ON classrooms (number);
--rollback DROP INDEX creator_id_index; DROP INDEX number_index;