package org.example.promotionservice.repository;

import org.example.promotionservice.entity.Outbox;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Repository
public class OutboxRepository {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public OutboxRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public boolean create(Outbox outbox) {
        String sql = """
                INSERT INTO outbox (id, promotion_id, event_type, created_at, processed)
                VALUES (:id, :promotion_id, :event_type, :created_at, :processed)
                """;
        return namedParameterJdbcTemplate.update(sql, Map.of(
                "id", outbox.id(),
                "promotion_id", outbox.promotionId(),
                "event_type", outbox.eventType(),
                "created_at", outbox.createdAt(),
                "processed", outbox.processed())) == 1;

    }

    public List<Outbox> findPending(Integer limit) {
        String sql = """
                SELECT id, promotion_id, event_type, created_at, processed
                FROM outbox
                WHERE processed = false
                ORDER BY created_at ASC
                LIMIT :limit
                """;

        return namedParameterJdbcTemplate.query(sql, Map.of("limit", limit), OUTBOX_ROW_MAPPER);
    }

    public boolean markProcessed(UUID id) {
        String sql = """
                UPDATE outbox
                SET processed = true
                WHERE id = :id
                """;
        return namedParameterJdbcTemplate.update(sql, Map.of("id", id)) == 1;
    }

    private static final RowMapper<Outbox> OUTBOX_ROW_MAPPER = (rs, rowNum) -> new Outbox(
            rs.getObject("id", UUID.class),
            rs.getObject("promotion_id", UUID.class),
            rs.getString("event_type"),
            rs.getObject("created_at", LocalDateTime.class),
            rs.getBoolean("processed")
    );
}
