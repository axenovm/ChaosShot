--liquibase formatted sql

--changeset order-service:008-add-shot-id-to-order-promotions
ALTER TABLE order_promotions
    ADD COLUMN IF NOT EXISTS shot_id UUID;
