package org.example.orderservice.repository;

import org.example.orderservice.entity.Order;
import org.example.orderservice.entity.User;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Repository
public class UserRepository {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public UserRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public Optional<User> findUserById(UUID id) {
        Objects.requireNonNull(id, "id must not be null");
        String sql = "select * from users where id = :id";
        return namedParameterJdbcTemplate.query(sql, Map.of("id", id), USER_ROW_MAPPER).stream().findFirst();
    }

    public List<User> findAllUsers() {
        return  namedParameterJdbcTemplate.query("select * from users", USER_ROW_MAPPER);
    }

    private static final RowMapper<User> USER_ROW_MAPPER = (rs, rowNum) -> new User(
            rs.getObject("id", UUID.class),
            rs.getString("email"),
            rs.getTimestamp("created_at") != null
                    ? rs.getTimestamp("created_at").toLocalDateTime()
                    : null,
            rs.getString("username"),
            rs.getString("password"));
}
