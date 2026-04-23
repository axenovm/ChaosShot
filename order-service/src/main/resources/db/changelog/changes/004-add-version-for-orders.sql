--liquibase formatted sql

--changeset maxim:add-orders-version-001

ALTER TABLE orders ADD COLUMN version INTEGER DEFAULT 0 NOT NULL;