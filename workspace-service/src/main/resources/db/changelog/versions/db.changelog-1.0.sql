--liquibase formatted sql

--changeset workspace:1
CREATE TABLE workspaces (

);
--rollback drop table workspaces;