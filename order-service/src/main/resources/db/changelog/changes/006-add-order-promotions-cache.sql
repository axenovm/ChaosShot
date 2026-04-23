--liquibase formatted sql

--changeset order-service:006-add-order-promotions-cache
--preconditions onFail:MARK_RAN
--precondition-sql-check expectedResult:0 SELECT COUNT(*) FROM information_schema.tables WHERE table_schema = current_schema() AND table_name = 'order_promotions'
CREATE TABLE order_promotions (
    promotion_id UUID NOT NULL,
    event_id UUID NOT NULL,
    name VARCHAR(255) NOT NULL,
    discount_percent INTEGER NOT NULL,
    starts_at TIMESTAMP NOT NULL,
    ends_at TIMESTAMP NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    CONSTRAINT pk_order_promotions PRIMARY KEY (promotion_id),
    CONSTRAINT uq_order_promotions_event UNIQUE (event_id)
);
