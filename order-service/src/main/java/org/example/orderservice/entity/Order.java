package org.example.orderservice.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record Order(UUID id,
                    BigDecimal totalAmount,
                    LocalDateTime createdAt,
                    UUID userId,
                    UUID barId,
                    String number,
                    Integer version) {}
