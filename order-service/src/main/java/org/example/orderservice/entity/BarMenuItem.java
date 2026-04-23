package org.example.orderservice.entity;

import java.math.BigDecimal;
import java.util.UUID;

public record BarMenuItem(UUID id,
                          UUID barId,
                          String name,
                          BigDecimal price) {}
