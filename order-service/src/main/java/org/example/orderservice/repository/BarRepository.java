package org.example.orderservice.repository;

import org.example.orderservice.entity.Bar;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Repository
public class BarRepository {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public BarRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public Optional<Bar> findBarById(UUID id) {
        String sql = "select * from bars where id=:id";
        return namedParameterJdbcTemplate.query(sql, Map.of("id", id), BAR_ROW_MAPPER).stream().findFirst();
    }

    public List<Bar> findAllBars() {
        String sql = "select * from bars";
        return namedParameterJdbcTemplate.query(sql, BAR_ROW_MAPPER);
    }

    private static final RowMapper<Bar> BAR_ROW_MAPPER = (rs, rowNum) -> new Bar(
            rs.getObject("id", UUID.class),
            rs.getString("bar_name"),
            rs.getString("address"));
}
