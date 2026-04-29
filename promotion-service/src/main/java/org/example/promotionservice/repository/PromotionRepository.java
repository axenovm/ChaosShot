package org.example.promotionservice.repository;

import org.example.promotionservice.entity.Promotion;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Repository
public class PromotionRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public PromotionRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.namedParameterJdbcTemplate = jdbcTemplate;
    }

    public Optional<Promotion> findByName(String name) {
        String sql = "SELECT * FROM promotions WHERE name = :name";
        return namedParameterJdbcTemplate.query(sql, Map.of("name", name), PROMOTION_ROW_MAPPER).stream().findFirst();
    }

    public Optional<Promotion> findById(UUID id) {
        String sql = """
            SELECT * FROM promotions 
            WHERE id = :id
            """;

        return namedParameterJdbcTemplate.query(sql, Map.of("id", id), PROMOTION_ROW_MAPPER)
                .stream()
                .findFirst();
    }

    public List<Promotion> findAll() {
        String sql = "SELECT * FROM promotions ORDER BY starts_at DESC";
        return namedParameterJdbcTemplate.query(sql, PROMOTION_ROW_MAPPER);
    }

    public List<Promotion> findActivePromotions() {
        String sql = """
            SELECT * FROM promotions 
            WHERE is_active = true 
              AND starts_at <= NOW() 
              AND ends_at >= NOW()
            ORDER BY starts_at DESC
            """;
        return namedParameterJdbcTemplate.query(sql, PROMOTION_ROW_MAPPER);
    }

    public List<Promotion> findActiveStartedBefore(LocalDateTime threshold) {
        String sql = """
            SELECT * FROM promotions
            WHERE is_active = true
              AND starts_at <= :threshold
            ORDER BY starts_at ASC
            """;
        return namedParameterJdbcTemplate.query(sql, Map.of("threshold", threshold), PROMOTION_ROW_MAPPER);
    }

    public boolean create(Promotion promotion) {
        String sql = """
            INSERT INTO promotions (id, name, discount_percent, starts_at, ends_at, is_active)
            VALUES (:id, :name, :discount_percent, :starts_at, :ends_at, :is_active)
            """;

        return namedParameterJdbcTemplate.update(sql, Map.of(
                "id", promotion.id(),
                "name", promotion.name(),
                "discount_percent", promotion.discountPercent(),
                "starts_at", promotion.startsAt(),
                "ends_at", promotion.endsAt(),
                "is_active", promotion.isActive())) == 1;
    }

    public boolean update(Promotion promotion) {
        String sql = """
            UPDATE promotions 
            SET name = :name,
                discount_percent = :discount_percent,
                starts_at = :starts_at,
                ends_at = :ends_at,
                is_active = :is_active
            WHERE id = :id
            """;

        return namedParameterJdbcTemplate.update(sql, Map.of(
                "id", promotion.id(),
                "name", promotion.name(),
                "discount_percent", promotion.discountPercent(),
                "starts_at", promotion.startsAt(),
                "ends_at", promotion.endsAt(),
                "is_active", promotion.isActive()
        )) == 1;
    }

    public boolean deleteById(UUID id) {
        String sql = "DELETE FROM promotions WHERE id = :id";
        return namedParameterJdbcTemplate.update(sql, Map.of("id", id)) == 1;
    }

    public boolean deactivateById(UUID id) {
        String sql = """
            UPDATE promotions
            SET is_active = false
            WHERE id = :id
              AND is_active = true
            """;
        return namedParameterJdbcTemplate.update(sql, Map.of("id", id)) == 1;
    }

    private static final RowMapper<Promotion> PROMOTION_ROW_MAPPER = (rs, rowNum) -> new Promotion(
            rs.getObject("id", UUID.class),
            rs.getString("name"),
            rs.getInt("discount_percent"),
            rs.getObject("starts_at", LocalDateTime.class),
            rs.getObject("ends_at", LocalDateTime.class),
            rs.getBoolean("is_active")
    );
}