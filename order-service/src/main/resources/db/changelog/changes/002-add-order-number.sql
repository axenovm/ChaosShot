--liquibase formatted sql

--changeset maxim:add-order-number-001
ALTER TABLE orders ADD COLUMN order_number VARCHAR(50);

--changeset maxim:order-number-not-null-003
ALTER TABLE orders ALTER COLUMN order_number SET NOT NULL;

--changeset maxim:order-number-unique-004
ALTER TABLE orders ADD CONSTRAINT uq_orders_order_number UNIQUE (order_number);