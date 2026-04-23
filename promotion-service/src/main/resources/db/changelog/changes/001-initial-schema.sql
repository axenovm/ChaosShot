--liquibase formatted sql

--changeset promotion-service:001-initial-schema
--preconditions onFail:MARK_RAN
--precondition-sql-check expectedResult:0 SELECT COUNT(*) FROM information_schema.tables WHERE table_schema = current_schema() AND table_name = 'promotions'
CREATE TABLE promotions (
    id UUID NOT NULL,
    name VARCHAR(255) NOT NULL,
    shot_type VARCHAR(255) NOT NULL,
    discount_percent INTEGER NOT NULL,
    starts_at TIMESTAMP NOT NULL,
    ends_at TIMESTAMP NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    CONSTRAINT pk_promotions PRIMARY KEY (id)
);

CREATE TABLE outbox (
    id UUID NOT NULL,
    promotion_id UUID NOT NULL,
    event_type VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    processed BOOLEAN NOT NULL DEFAULT FALSE,
    CONSTRAINT pk_outbox PRIMARY KEY (id),
    CONSTRAINT fk_outbox_promotion FOREIGN KEY (promotion_id) REFERENCES promotions (id) ON DELETE CASCADE
);
