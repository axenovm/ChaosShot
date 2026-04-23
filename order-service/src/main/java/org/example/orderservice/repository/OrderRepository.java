package org.example.orderservice.repository;

import org.example.orderservice.DTO.response.PageResponse;
import org.example.orderservice.entity.Order;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class OrderRepository {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public OrderRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public boolean deleteOrderById(UUID id) {
        String sql = "DELETE FROM orders WHERE id = :id";
        int rows = namedParameterJdbcTemplate.update(sql, Map.of("id", id));
        return rows > 0;
    }

    public Order updateOrder(Order order) {
        String sql = """
            UPDATE orders 
            SET total_amount = :totalAmount,
                user_id = :userId,
                bar_id = :barId,
                order_number = :orderNumber,
                version = version + 1
            WHERE id = :id AND version = :version
            RETURNING *
            """;

            Map<String, Object> params = Map.of(
                    "id", order.id(),
                    "totalAmount", order.totalAmount(),
                    "userId", order.userId(),
                    "barId", order.barId(),
                    "orderNumber", order.number(),
                    "version", order.version()
            );

            try {
                return namedParameterJdbcTemplate.queryForObject(sql, params, ORDER_ROW_MAPPER);
            } catch (EmptyResultDataAccessException e) {
                throw new OptimisticLockingFailureException(
                        "Order was modified by another transaction. Current version: " + order.version(), e);
            }

    }

    public PageResponse searchOrders(UUID userId, UUID barId, int page, int size) {
        StringBuilder whereClause = new StringBuilder(" WHERE 1=1 ");
        Map<String, Object> params = new HashMap<>();

        if (userId != null) {
            whereClause.append(" AND user_id = :userId ");
            params.put("userId", userId);
        }
        if (barId != null) {
            whereClause.append(" AND bar_id = :barId ");
            params.put("barId", barId);
        }

        String contentSql = "SELECT * FROM orders"
                + whereClause
                + " ORDER BY created_at DESC LIMIT :limit OFFSET :offset";
        String countSql = "SELECT COUNT(*) FROM orders" + whereClause;

        params.put("limit", size);
        params.put("offset", page * size);

        List<Order> content = namedParameterJdbcTemplate.query(contentSql, params, ORDER_ROW_MAPPER);
        Long totalElements = namedParameterJdbcTemplate.queryForObject(countSql, params, Long.class);
        long safeTotalElements = totalElements == null ? 0 : totalElements;
        int totalPages = (int) Math.ceil((double) safeTotalElements / size);
        boolean hasNext = page + 1 < totalPages;

        return new PageResponse(content, page, size, safeTotalElements, totalPages, hasNext);
    }

    public Optional<Order> findOrdersByNumber(String number)
    {
       String sql="select * from orders where order_number=:number";
       return namedParameterJdbcTemplate.query(sql, Map.of("number", number), ORDER_ROW_MAPPER).stream().findFirst();
    }

    public boolean createOrder(Order order) {
        String sql = "INSERT INTO orders (id, total_amount, created_at, user_id, bar_id, order_number, version)" +
                "VALUES (:id, :total_amount, :created_at, :user_id, :bar_id, :order_number, :version)";
        return namedParameterJdbcTemplate.update(sql, Map.of(
                "id", order.id(),
                "total_amount", order.totalAmount(),
                "created_at", order.createdAt(),
                "user_id", order.userId(),
                "bar_id", order.barId(),
                "order_number", order.number(),
                "version", order.version())
        ) == 1;
    }

    public List<Order> findOrdersByUserId(UUID user_id) {
        String sql = "select * from orders where user_id=:user_id";
        return namedParameterJdbcTemplate.query(sql, Map.of("user_id", user_id), ORDER_ROW_MAPPER);
    }

    public List<Order> findAllOrders() {
        String sql = "select * from orders";
        return namedParameterJdbcTemplate.query(sql, ORDER_ROW_MAPPER);
    }

    public Optional<Order> findOrderById(UUID id) {
        String sql = "select * from orders where id=:id";
        return namedParameterJdbcTemplate.query(sql, Map.of("id", id), ORDER_ROW_MAPPER).stream().findFirst();
    }

    private static final RowMapper<Order> ORDER_ROW_MAPPER = (rs, rowNum) -> new Order(
            rs.getObject("id", UUID.class),
            rs.getBigDecimal("total_amount"),
            rs.getTimestamp("created_at") != null
                    ? rs.getTimestamp("created_at").toLocalDateTime()
                    : null,
            rs.getObject("user_id", UUID.class),
            rs.getObject("bar_id", UUID.class),
            rs.getString("order_number"),
            rs.getInt("version"));
}
