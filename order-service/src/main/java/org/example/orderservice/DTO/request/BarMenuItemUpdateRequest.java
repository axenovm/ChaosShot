package org.example.orderservice.DTO.request;

import java.math.BigDecimal;
import java.util.UUID;

public record BarMenuItemUpdateRequest(
        UUID id,
        BigDecimal price,
        Integer discount_percent) {}
