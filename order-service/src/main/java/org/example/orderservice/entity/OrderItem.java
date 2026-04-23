package org.example.orderservice.entity;

import java.math.BigDecimal;
import java.util.UUID;

public record OrderItem(UUID id,
                        String shotType,
                        Integer quantity,
                        BigDecimal price,
                        UUID orderId) {}
