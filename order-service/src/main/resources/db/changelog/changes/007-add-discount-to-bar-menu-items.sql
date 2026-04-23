--liquibase formatted sql

--changeset order-service:007-add-discount-to-bar-menu-items
ALTER TABLE bar_menu_items
    ADD COLUMN IF NOT EXISTS discount_percent INTEGER NOT NULL DEFAULT 0;
