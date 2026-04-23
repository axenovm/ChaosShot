package org.example.orderservice.repository;

import org.example.orderservice.entity.BarMenuItem;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Repository
public class BarMenuItemRepository {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public BarMenuItemRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public List<BarMenuItem> findMenuByBarId(UUID barId) {
        String sql = "SELECT id, bar_id, name, price FROM bar_menu_items WHERE bar_id = :barId";
        return namedParameterJdbcTemplate.query(sql, Map.of("barId", barId), MENU_ROW_MAPPER);
    }

    public Map<UUID, BarMenuItem> findByBarIdAndMenuItemIds(UUID barId, Collection<UUID> ids) {
        if (ids.isEmpty()) {
            return Map.of();
        }
        String sql = """
                SELECT id, bar_id, name, price
                FROM bar_menu_items
                WHERE bar_id = :barId AND id IN (:ids)
                """;
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("barId", barId)
                .addValue("ids", ids);
        List<BarMenuItem> list = namedParameterJdbcTemplate.query(sql, params, MENU_ROW_MAPPER);
        return list.stream().collect(Collectors.toMap(BarMenuItem::id, Function.identity()));
    }

    private static final RowMapper<BarMenuItem> MENU_ROW_MAPPER = (rs, rowNum) -> new BarMenuItem(
            rs.getObject("id", UUID.class),
            rs.getObject("bar_id", UUID.class),
            rs.getString("name"),
            rs.getBigDecimal("price"));
}
