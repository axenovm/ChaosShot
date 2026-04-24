--liquibase formatted sql

--changeset order-service:009-add-fk-order-promotions-shot-id
--preconditions onFail:MARK_RAN
--precondition-sql-check expectedResult:0 SELECT COUNT(*) FROM information_schema.table_constraints WHERE table_schema = current_schema() AND table_name = 'order_promotions' AND constraint_name = 'fk_order_promotions_shot'
ALTER TABLE order_promotions
    ADD CONSTRAINT fk_order_promotions_shot
    FOREIGN KEY (shot_id)
    REFERENCES bar_menu_items (id)
    ON DELETE SET NULL;
