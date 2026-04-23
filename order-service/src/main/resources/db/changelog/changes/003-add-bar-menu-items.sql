--liquibase formatted sql

--changeset maxim:add-bar-menu-items-001
--validCheckSum: 9:b9900f644a8376d729c097c9865e599b
--validCheckSum: 9:055772c432e85a2ac515ed63830b6dee
CREATE TABLE bar_menu_items (
    id UUID NOT NULL,
    bar_id UUID NOT NULL,
    name VARCHAR(255) NOT NULL,
    price NUMERIC(19, 2) NOT NULL,
    CONSTRAINT pk_bar_menu_items PRIMARY KEY (id),
    CONSTRAINT fk_bar_menu_items_bar FOREIGN KEY (bar_id) REFERENCES bars (id)
);
