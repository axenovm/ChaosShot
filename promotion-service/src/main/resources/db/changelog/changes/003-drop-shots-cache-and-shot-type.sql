--liquibase formatted sql

--changeset promotion-service:003-drop-shots-cache-and-shot-type
ALTER TABLE promotions DROP COLUMN IF EXISTS shot_type;
DROP TABLE IF EXISTS shots_cache;
