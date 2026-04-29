package org.example.orderservice.repository;

import lombok.RequiredArgsConstructor;
import org.example.orderservice.entity.OrderPromotion;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class OrderPromotionRepository {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public Optional<OrderPromotion> findById(UUID eventId) {
        String sql = "SELECT * FROM order_promotions WHERE event_id = :event_id";
        return namedParameterJdbcTemplate.query(sql, Map.of("event_id", eventId), ORDER_PROMOTION_ROW_MAPPER)
                .stream()
                .findFirst();
    }

    public Optional<OrderPromotion> findByPromotionId(UUID promotionId) {
        String sql = "SELECT * FROM order_promotions WHERE promotion_id = :promotion_id";
        return namedParameterJdbcTemplate.query(sql, Map.of("promotion_id", promotionId), ORDER_PROMOTION_ROW_MAPPER)
                .stream()
                .findFirst();
    }

    public List<OrderPromotion> findActiveByShotId(UUID shotId) {
        String sql = """
                SELECT * FROM order_promotions
                WHERE shot_id = :shot_id
                  AND is_active = true
                """;
        return namedParameterJdbcTemplate.query(sql, Map.of("shot_id", shotId), ORDER_PROMOTION_ROW_MAPPER);
    }

    public boolean deactivatePromotion(UUID eventId) {
        String sql = "UPDATE order_promotions SET is_active = false WHERE event_id = :event_id";
        return namedParameterJdbcTemplate.update(sql, Map.of("event_id", eventId)) == 1;
    }

    public boolean deactivatePromotionByPromotionId(UUID promotionId) {
        String sql = "UPDATE order_promotions SET is_active = false WHERE promotion_id = :promotion_id AND is_active = true";
        return namedParameterJdbcTemplate.update(sql, Map.of("promotion_id", promotionId)) == 1;
    }

    public boolean upsert(OrderPromotion orderPromotion) {
        String sql = """
                INSERT INTO order_promotions (promotion_id, shot_id, event_id, name, discount_percent, starts_at, ends_at, is_active)
                VALUES (:promotion_id, :shot_id, :event_id, :name, :discount_percent, :starts_at, :ends_at, :is_active)
                ON CONFLICT (promotion_id) DO UPDATE
                SET shot_id = EXCLUDED.shot_id,
                    event_id = EXCLUDED.event_id,
                    name = EXCLUDED.name,
                    discount_percent = EXCLUDED.discount_percent,
                    starts_at = EXCLUDED.starts_at,
                    ends_at = EXCLUDED.ends_at,
                    is_active = EXCLUDED.is_active
                """;
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("promotion_id", orderPromotion.promotionId())
                .addValue("shot_id", orderPromotion.shotId())
                .addValue("event_id", orderPromotion.eventId())
                .addValue("name", orderPromotion.name())
                .addValue("discount_percent", orderPromotion.discountPercent())
                .addValue("starts_at", orderPromotion.startsAt())
                .addValue("ends_at", orderPromotion.endsAt())
                .addValue("is_active", orderPromotion.active());
        return namedParameterJdbcTemplate.update(sql, params) == 1;
    }

    private static final RowMapper<OrderPromotion> ORDER_PROMOTION_ROW_MAPPER = (rs, rowNum) -> new OrderPromotion(
            rs.getObject("promotion_id", UUID.class),
            rs.getObject("shot_id", UUID.class),
            rs.getObject("event_id", UUID.class),
            rs.getString("name"),
            rs.getInt("discount_percent"),
            rs.getObject("starts_at", LocalDateTime.class),
            rs.getObject("ends_at", LocalDateTime.class),
            rs.getBoolean("is_active")
    );
}
