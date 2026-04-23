package org.example.orderservice.repository;

import lombok.RequiredArgsConstructor;
import org.example.orderservice.entity.OrderPromotion;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
@RequiredArgsConstructor
public class OrderPromotionRepository {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public boolean upsert(OrderPromotion orderPromotion) {
        String sql = """
                INSERT INTO order_promotions (promotion_id, event_id, name, discount_percent, starts_at, ends_at, is_active)
                VALUES (:promotion_id, :event_id, :name, :discount_percent, :starts_at, :ends_at, :is_active)
                ON CONFLICT (promotion_id) DO UPDATE
                SET event_id = EXCLUDED.event_id,
                    name = EXCLUDED.name,
                    discount_percent = EXCLUDED.discount_percent,
                    starts_at = EXCLUDED.starts_at,
                    ends_at = EXCLUDED.ends_at,
                    is_active = EXCLUDED.is_active
                """;
        return namedParameterJdbcTemplate.update(sql, Map.of(
                "promotion_id", orderPromotion.promotionId(),
                "event_id", orderPromotion.eventId(),
                "name", orderPromotion.name(),
                "discount_percent", orderPromotion.discountPercent(),
                "starts_at", orderPromotion.startsAt(),
                "ends_at", orderPromotion.endsAt(),
                "is_active", orderPromotion.active())
        ) == 1;
    }
}
