package org.example.orderservice.repository;

import org.example.orderservice.DTO.request.BarMenuItemUpdateRequest;
import org.example.orderservice.entity.BarMenuItem;
import org.example.orderservice.entity.Order;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Repository
public class BarMenuItemRepository {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public BarMenuItemRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public BarMenuItem updateItemPrice(BarMenuItemUpdateRequest barMenuItemUpdateRequest) {
        String sql = """
            UPDATE bar_menu_items
            SET price = :price,
                discount_percent = :discount_percent
            WHERE id = :id
            """;
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", barMenuItemUpdateRequest.id())
                .addValue("price", barMenuItemUpdateRequest.price())
                .addValue("discount_percent", barMenuItemUpdateRequest.discount_percent());
        namedParameterJdbcTemplate.update(sql, params);
        return findById(barMenuItemUpdateRequest.id())
                .orElseThrow(() -> new EmptyResultDataAccessException("Bar menu item not found after update", 1));
    }

    public Optional<BarMenuItem> findById(UUID id) {
        String sql = "SELECT * FROM bar_menu_items WHERE id = :id";
        return namedParameterJdbcTemplate.query(sql, Map.of("id", id), MENU_ROW_MAPPER).stream().findFirst();
    }

    public List<BarMenuItem> findMenuByBarId(UUID barId) {
        String sql = "SELECT id, bar_id, name, price FROM bar_menu_items WHERE bar_id = :barId";
        return namedParameterJdbcTemplate.query(sql, Map.of("barId", barId), MENU_ROW_MAPPER);
    }

    public Map<UUID, BarMenuItem> findByBarIdAndMenuItemIds(UUID barId, List<UUID> ids) {
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
