--liquibase formatted sql

--changeset order-service:001-initial-schema
--preconditions onFail:MARK_RAN
--precondition-sql-check expectedResult:0 SELECT COUNT(*) FROM information_schema.tables WHERE table_schema = current_schema() AND table_name = 'users'
CREATE TABLE users (
    id UUID NOT NULL,
    email VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    username VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    CONSTRAINT pk_users PRIMARY KEY (id),
    CONSTRAINT uk_users_email UNIQUE (email)
);

CREATE TABLE bars (
    id UUID NOT NULL,
    bar_name VARCHAR(255) NOT NULL,
    address VARCHAR(512),
    CONSTRAINT pk_bars PRIMARY KEY (id)
);

CREATE TABLE orders (
    id UUID NOT NULL,
    total_amount NUMERIC(19, 2) NOT NULL,
    status VARCHAR(64) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    user_id UUID NOT NULL,
    bar_id UUID NOT NULL,
    CONSTRAINT pk_orders PRIMARY KEY (id),
    CONSTRAINT fk_orders_user FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT fk_orders_bar FOREIGN KEY (bar_id) REFERENCES bars (id)
);

CREATE TABLE order_items (
    id UUID NOT NULL,
    shot_type VARCHAR(255) NOT NULL,
    quantity INTEGER NOT NULL,
    price NUMERIC(19, 2) NOT NULL,
    order_id UUID NOT NULL,
    CONSTRAINT pk_order_items PRIMARY KEY (id),
    CONSTRAINT fk_order_items_order FOREIGN KEY (order_id) REFERENCES orders (id)
);
