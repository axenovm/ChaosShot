package org.example.orderservice.repository;

import org.example.orderservice.entity.OrderItem;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Repository
public class OrderItemRepository {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public OrderItemRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public List<OrderItem> findAllItems() {
        String sql = "select * from order_items";
        return namedParameterJdbcTemplate.query(sql, ITEM_ROW_MAPPER);
    }

    public Optional<OrderItem> findOrderItemById(UUID id) {
        String sql = "select * from order_items where id=:id";
        return namedParameterJdbcTemplate.query(sql, Map.of("id", id), ITEM_ROW_MAPPER).stream().findFirst();
    }

    public List<OrderItem> findOrderItemByOrderId(UUID order_id) {
        String sql = "select * from order_items where order_id=:order_id";
        return namedParameterJdbcTemplate.query(sql, Map.of("order_id", order_id), ITEM_ROW_MAPPER);
    }

    public boolean insertOrderItem(OrderItem item) {
        String sql = """
                INSERT INTO order_items (id, shot_type, quantity, price, order_id)
                VALUES (:id, :shot_type, :quantity, :price, :order_id)
                """;
        return namedParameterJdbcTemplate.update(sql, Map.of(
                "id", item.id(),
                "shot_type", item.shotType(),
                "quantity", item.quantity(),
                "price", item.price(),
                "order_id", item.orderId())) == 1;
    }

    public int deleteOrderItemsByOrderId(UUID orderId) {
        String sql = "DELETE FROM order_items WHERE order_id = :orderId";
        return namedParameterJdbcTemplate.update(sql, Map.of("orderId", orderId));
    }

    private static final RowMapper<OrderItem> ITEM_ROW_MAPPER = (rs, rowNum) -> new OrderItem(
            rs.getObject("id", UUID.class),
            rs.getString("shot_type"),
            rs.getInt("quantity"),
            rs.getBigDecimal("price"),
            rs.getObject("order_id", UUID.class));

}
