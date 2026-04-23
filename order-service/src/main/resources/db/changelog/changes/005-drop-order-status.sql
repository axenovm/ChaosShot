--liquibase formatted sql

--changeset chaosShot:005-drop-order-status-001
ALTER TABLE orders DROP COLUMN IF EXISTS status;
